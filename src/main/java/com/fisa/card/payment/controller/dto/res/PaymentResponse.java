package com.fisa.card.payment.controller.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private String authorizationId; // txnId → 문자열로 전달
    private String status;          // "APPROVED"
    private String message;         // "Payment authorized"
}
