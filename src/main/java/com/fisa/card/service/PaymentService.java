package com.fisa.card.service;


import com.fisa.card.dto.req.BankWithdrawRequest;
import com.fisa.card.dto.req.PaymentRequest;
import com.fisa.card.dto.req.RefundRequest;
import com.fisa.card.dto.res.BankWithdrawResponse;
import com.fisa.card.dto.res.PaymentResponse;
import com.fisa.card.dto.res.PaymentResultResponse;
import com.fisa.card.dto.res.RefundResponse;
import com.fisa.card.entity.*;
import com.fisa.card.openfeign.CardClient;
import com.fisa.card.repository.BinInfoRepository;
import com.fisa.card.repository.CardRepository;
import com.fisa.card.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CardRepository cardRepository;

    private final BinInfoRepository binInfoRepository;

    private final CardClient cardClient;


    @Value("${bank.withdraw.url}")
    private String bankWithdrawUrl;

    @Transactional
    public PaymentResultResponse authorizePayment(PaymentRequest request) {


        log.info("✅ 결제 승인 요청 수신: txnId={}, cardNumber={}, amount={}, settlementAccountNumber={}",
                request.getTxnId(),
                request.getCardNumber(),
                request.getAmount(),
                request.getSettlementAccountNumber());
        // 1. 카드 조회 및 유효성 검사
        Card card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드번호가 존재하지 않습니다."));

        // 카드 만료일 검사 (이번 달 이전이면 유효하지 않음)
        YearMonth expiry = YearMonth.from(card.getExpiredAt());
        YearMonth current = YearMonth.now();

        if (expiry.isBefore(current)) {
            throw new IllegalArgumentException("카드 유효기간이 초과되었습니다.");
        }

        // 2. 결제 내역 저장 (PENDING)
        Payment payment = Payment.builder()
                .txnId(request.getTxnId())
                .amount(request.getAmount())
                .depositAccount(request.getSettlementAccountNumber())
                .cardNumber(request.getCardNumber())
                .paymentStatus(PaymentStatus.PENDING)
                .charged(false)
                .build();

        Payment saved = paymentRepository.save(payment);

        String bin = request.getCardNumber().substring(0, 6);

        //카드 BIN 값으로 신용카드인지 체크카드인지 판단
        BinInfo binInfo = binInfoRepository.findByBin(bin)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카드 BIN 정보입니다."));

        // 3. 카드 타입 분기 처리
        try {
            switch (binInfo.getCardType()) {
                case DEBIT -> {
                    // 체크카드 처리 (은행 출금 요청)
                    BankWithdrawRequest bankReq = new BankWithdrawRequest(
                            card.getAccountNumber(),
                            request.getAmount()
                    );

                    BankWithdrawResponse response = cardClient.withdrawFromBank(bankReq);

                    if ("SUCCESS".equals(response.getStatus())) {
                        saved.updatePaymentStatus(PaymentStatus.SUCCEEDED);
                        saved.updateCharged(true);
                    } else {
                        saved.updatePaymentStatus(PaymentStatus.FAILED);
                    }

                    return buildResponse(saved);
                }

                case CREDIT -> {
                    // 신용카드 처리 (한도 확인 후 결제 예약)
                    Long unchargedTotal = paymentRepository.findUnchargedTotalByCardNumber(card.getCardNumber());
                    Long availableLimit = card.getCardLimit() - unchargedTotal;

                    if (availableLimit < request.getAmount()) {
                        saved.updatePaymentStatus(PaymentStatus.FAILED);
                    } else {
                        saved.updatePaymentStatus(PaymentStatus.SUCCEEDED);
                    }

                    return buildResponse(saved);
                }

                default -> {
                    log.error("알 수 없는 카드 타입: {}", binInfo.getCardType());
                    saved.updatePaymentStatus(PaymentStatus.FAILED);
                    return buildResponse(saved);
                }
            }
        } catch (Exception e) {
            log.error("결제 처리 중 예외 발생", e);
            saved.updatePaymentStatus(PaymentStatus.FAILED);
            return buildResponse(saved);
        }
    }

    @Transactional
    public RefundResponse refundPayment(RefundRequest request) {
        // 1. 기존 결제 내역 조회
        Payment payment = paymentRepository.findByTxnId(request.getTxnId())
                .orElseThrow(() -> new IllegalArgumentException("해당 거래 ID의 결제 내역이 없습니다."));
        Card card = cardRepository.findByCardNumber(payment.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드 정보를 찾을 수 없습니다."));

        // 2. 결제 상태 확인 (성공한 거래만 환불 가능)
        if (payment.getPaymentStatus() != PaymentStatus.SUCCEEDED) {
            return RefundResponse.builder()
                    .txnId(payment.getTxnId())
                    .paymentStatus(PaymentStatus.FAILED)
                    .build();
        }

        // 3. 카드 BIN 정보로 카드 타입 판단
        String bin = payment.getCardNumber().substring(0, 6);
        BinInfo binInfo = binInfoRepository.findByBin(bin)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카드 BIN 정보입니다."));

        try {
            switch (binInfo.getCardType()) {
                case DEBIT -> {

                    // 체크카드 환불 → 은행에 입금 요청
                    BankWithdrawRequest depositReq = new BankWithdrawRequest(
                            card.getAccountNumber(),
                            payment.getAmount()
                    );

                    BankWithdrawResponse bankResponse = cardClient.depositToBank(depositReq); // 은행 입금 요청

                    if ("SUCCESS".equals(bankResponse.getStatus())) {
                        payment.updatePaymentStatus(PaymentStatus.CANCELLED);
                        payment.updateCharged(false);
                    } else {
                        return RefundResponse.builder()
                                .txnId(payment.getTxnId())
                                .paymentStatus(PaymentStatus.FAILED)
                                .build();
                    }
                }

                case CREDIT -> {
                    // 신용카드 환불 → 예약 취소만 가능
                    if (!payment.isCharged()) {
                        payment.updatePaymentStatus(PaymentStatus.CANCELLED);
                    } else {
                        return RefundResponse.builder()
                                .txnId(payment.getTxnId())
                                .paymentStatus(PaymentStatus.FAILED)
                                .build();
                    }
                }

                default -> {
                    throw new IllegalStateException("알 수 없는 카드 타입입니다.");
                }
            }

            return RefundResponse.builder()
                    .txnId(payment.getTxnId())
                    .paymentStatus(payment.getPaymentStatus())
                    .build();

        } catch (Exception e) {
            log.error("환불 처리 중 예외 발생", e);
            return RefundResponse.builder()
                    .txnId(payment.getTxnId())
                    .paymentStatus(PaymentStatus.FAILED)
                    .build();
        }
    }

    private PaymentResultResponse buildResponse(Payment payment) {
        return PaymentResultResponse.builder()
                .txnId(payment.getTxnId())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    /*
      결제 요청 내역 조회
     */
    @Transactional
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(payment -> PaymentResponse.builder()
                        .txnId(payment.getTxnId())
                        .paymentStatus(payment.getPaymentStatus())
                        .amount(payment.getAmount())
                        .charged(payment.isCharged())
                        .cardNumber(payment.getCardNumber())
                        .depositAccount(payment.getDepositAccount())
                        .build()
                ).toList();
    }


}
