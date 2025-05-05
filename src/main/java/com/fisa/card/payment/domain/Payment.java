package com.fisa.card.payment.domain;

import com.fisa.card.payment.domain.enums.TransactionStatus;
import com.fisa.card.card.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    private Long paymentId;

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


    /**
     * 승인인 결과가 처리된 후 update할 칼럼
     */

    public void setCompleteAt(LocalDateTime completeAt) {
        this.completeAt = completeAt;
    }

    public void setTransactionStatus(TransactionStatus status) {
        this.transactionStatus = status;
    }
}