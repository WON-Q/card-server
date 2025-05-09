package com.fisa.card.payment.controller.dto.res;

import com.fisa.card.card.domain.enums.CardType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private String txnId;
    private String status;
    private Long amount;
    private CardType cardType;
    private String cardNumber;
    private String merchant;
}
