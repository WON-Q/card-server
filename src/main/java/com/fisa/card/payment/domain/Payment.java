package com.fisa.card.payment.domain;

import com.fisa.card.payment.domain.enums.TransactionStatus;
import com.fisa.card.card.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    private Long paymentId;

    private String merchant;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private String cardNumber;

    private String expiryDate;

    private String cvv;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime completeAt;


}