package com.fisa.card.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * 결제 요청 정보를 저장하는 엔티티
 */
@Table(name = "payment")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    /**
     * PG사가 생성한 고유한 거래 식별자 (트랜잭션 ID)
     */
    @Id
    @Column(name = "txn_id", nullable = false)
    private String txnId;

    /**
     * 결제 금액 (단위: 원)
     */
    @Column(nullable = false)
    private Long amount;

    /**
     * 결제에 사용된 사용자의 카드 번호
     */
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    /**
     * 결제된 금액을 입금해줘야 하는 계좌번호
     */
    @Column(name = "deposit_account", nullable = false)
    private String depositAccount;

    /**
     * 결제 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    /**
     * 은행에 청구 여부
     */
    @Column(nullable = false)
    private boolean charged;

    /**
     * 결제 상태를 변경하는 메서드
     */
    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    /**
     * 은행 청구 여부 상태값 변경하는 메서드
     */
    public void updateCharged(boolean charged){this.charged=charged;}

}
