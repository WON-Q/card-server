package com.fisa.card.service;

import com.fisa.card.dto.req.BankWithdrawRequest;
import com.fisa.card.dto.req.PaymentRequest;
import com.fisa.card.dto.res.BankWithdrawResponse;
import com.fisa.card.dto.res.PaymentResponse;
import com.fisa.card.dto.res.PaymentResultResponse;
import com.fisa.card.entity.*;
import com.fisa.card.repository.BinInfoRepository;
import com.fisa.card.repository.CardRepository;
import com.fisa.card.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CardRepository cardRepository;

    private final RestTemplate restTemplate;

    private final BinInfoRepository binInfoRepository;


    @Value("${bank.withdraw.url}")
    private String bankWithdrawUrl;

    @Transactional
    public PaymentResultResponse authorizePayment(PaymentRequest request) {
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
                .depositAccount(request.getDepositAccount())
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
            // 체크 카드일 경우
            if (binInfo.getCardType() == CardType.DEBIT) {
                //은행에 출금 요청
                BankWithdrawRequest bankReq = new BankWithdrawRequest(
                        card.getAccountNumber(),
                        request.getAmount()
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<BankWithdrawRequest> httpEntity = new HttpEntity<>(bankReq, headers);

                ResponseEntity<BankWithdrawResponse> response = restTemplate.exchange(
                        bankWithdrawUrl,
                        HttpMethod.POST,
                        httpEntity,
                        BankWithdrawResponse.class
                );
                        /*
                          응답 성공했을떄
                         */
                if (response.getStatusCode().is2xxSuccessful()
                        && response.getBody() != null
                        && "SUCCESS".equals(response.getBody().getStatus())) {

                    saved.updatePaymentStatus(PaymentStatus.SUCCESS);
                    saved.updateCharged(true);

                    return buildResponse(saved);
                } else {
                    saved.updatePaymentStatus(PaymentStatus.FAILED);
                    return buildResponse(saved);
                }

            } else if (binInfo.getCardType() == CardType.CREDIT) {

                // 1) 미청구 결제 총액 조회
                Long unchargedTotal = paymentRepository.findUnchargedTotalByCardNumber(card.getCardNumber());
                Long availableLimit = card.getCardLimit() - unchargedTotal;

                if (availableLimit < request.getAmount()) {
                    saved.updatePaymentStatus(PaymentStatus.FAILED);
                    return buildResponse(saved);
                }
                // 신용카드 처리 (예약 저장)
                saved.updatePaymentStatus(PaymentStatus.SUCCESS);
                return buildResponse(saved);
            }

        } catch (Exception e) {
            log.error("결제 처리 중 예외 발생", e);
            saved.updatePaymentStatus(PaymentStatus.FAILED);
            return buildResponse(saved);
        }


        saved.updatePaymentStatus(PaymentStatus.FAILED);
        return buildResponse(saved);
    }


    private PaymentResultResponse buildResponse(Payment payment) {
        return PaymentResultResponse.builder()
                .txnId(payment.getTxnId())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }




}
