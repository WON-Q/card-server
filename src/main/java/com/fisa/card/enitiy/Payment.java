package com.fisa.card.enitiy;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 결제 요청 정보를 저장하는 엔티티 클래스입니다.
 * <p>
 * 각 결제는 트랜잭션 ID, 가맹점, 결제 금액, 카드 번호, 결제 상태 등을 포함합니다.
 * 카드 결제 흐름의 기록 및 정산 처리에 활용됩니다.
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    /**
     * 트랜잭션 ID (PK)
     * <br/>PG사  생성한 고유한 거래 식별자
     */
    @Id
    @Column(nullable = false)
    private String txnId;

    /**
     * 가맹점 ID
     * <br/>결제가 발생한 매장의 식별자
     */
    @Column(nullable = false)
    private Long merchantId;

    /**
     * 결제 금액 (단위: 원)
     */
    @Column(nullable = false)
    private Long amount;

    /**
     * 결제에 사용된 카드 번호
     */
    @Column(nullable = false)
    private String cardNumber;

    /**
     * 결제 상태
     * <br/>예: APPROVED, FAILED, PENDING 등
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
}
