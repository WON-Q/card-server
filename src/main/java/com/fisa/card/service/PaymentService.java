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


        return buildResponse(saved);
    }


    private PaymentResultResponse buildResponse(Payment payment) {
        return PaymentResultResponse.builder()
                .txnId(payment.getTxnId())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }




}
