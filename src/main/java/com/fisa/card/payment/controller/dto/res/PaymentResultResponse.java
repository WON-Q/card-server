package com.fisa.card.payment.controller.dto.res;

import com.fisa.card.payment.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResultResponse {
    private String transactionId; // txnId → 문자열로 전달
    private PaymentStatus paymentStatus;       // "APPROVED"
}
