package com.fisa.card.payment.controller.dto.res;


import com.fisa.card.card.domain.enums.CardType;
import com.fisa.card.payment.domain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    private Long txnId;
    private Long paymentId;
    private Long amount;
    private String currency;
    private CardType cardType;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private TransactionStatus transactionStatus;
    private LocalDateTime requestedAt;
    private LocalDateTime completeAt;
}