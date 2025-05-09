package com.fisa.card.payment.domain;

import com.fisa.card.payment.domain.enums.PaymentStatus;
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
    private Long paymentId;

    private String txnId;

    private String merchant;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    private LocalDateTime completeAt;


}