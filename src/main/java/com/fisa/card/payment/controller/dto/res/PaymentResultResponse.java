package com.fisa.card.payment.controller.dto.res;

import com.fisa.card.payment.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResultResponse {
    private String txnId;
    private PaymentStatus paymentStatus;
}
