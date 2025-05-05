// PaymentService.java
package com.fisa.card.payment.service;

import com.fisa.card.card.domain.Card;
import com.fisa.card.card.domain.enums.CardType;
import com.fisa.card.card.repository.CardRepository;
import com.fisa.card.payment.domain.Payment;
import com.fisa.card.payment.domain.enums.TransactionStatus;
import com.fisa.card.payment.controller.dto.req.PaymentRequest;
import com.fisa.card.payment.controller.dto.res.PaymentResponse;
import com.fisa.card.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;

    @Transactional
    public PaymentResponse authorizePayment(String jwtToken, PaymentRequest request) {
        // 1. 카드 유효성 검사
        Card card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드번호가 존재하지 않습니다."));

        // 만료일 검사
        YearMonth expiry = YearMonth.parse(request.getExpiryDate(), DateTimeFormatter.ofPattern("MM/yy"));
        if (expiry.isBefore(YearMonth.now())) {
            throw new IllegalArgumentException("카드 유효기간이 만료되었습니다.");
        }

        // CVV 검사
        if (!card.getCardCvv().equals(request.getCvv())) {
            throw new IllegalArgumentException("CVV 번호가 일치하지 않습니다.");
        }

        // 한도 검사
        if (card.getCardType() == CardType.CREDIT && request.getAmount() > card.getCardLimit()) {
            throw new IllegalArgumentException("카드 한도를 초과했습니다.");
        }

        // 2. 결제 내역 저장 (상태: PENDING)
        Payment payment = Payment.builder()
                .paymentId(request.getPaymentId())
                .amount(request.getAmount())
                .cardType(request.getCardType())
                .cardNumber(request.getCardNumber())
                .expiryDate(request.getExpiryDate())
                .cvv(request.getCvv())
                .requestedAt(request.getRequestedAt())
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);

        return PaymentResponse.builder()
                .txnId(saved.getTxnId())
                .paymentId(saved.getPaymentId())
                .amount(saved.getAmount())
                .cardType(saved.getCardType())
                .cardNumber(saved.getCardNumber())
                .transactionStatus(saved.getTransactionStatus())
                .requestedAt(saved.getRequestedAt())
                .completeAt(saved.getCompleteAt())
                .build();
    }
}
