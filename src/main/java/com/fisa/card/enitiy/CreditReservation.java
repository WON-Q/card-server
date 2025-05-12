package com.fisa.card.enitiy;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 신용카드 승인 예약 정보를 저장하는 엔티티 클래스입니다.
 * 신용카드 결제 내역을 정해진 날에 일괄처리 하기 위해 저장한 테이블
 */
@Entity
@Table(name = "credit_reservation")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreditReservation {

    /**
     * 예약 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 트랜잭션 ID
     * <br/>해당 예약이 속한 결제 트랜잭션의 고유 ID
     */
    @Column(nullable = false)
    private String txnId;

    /**
     * 카드 번호
     * <br/>신용 한도를 예약한 카드의 번호
     */
    @Column(nullable = false)
    private String cardNumber;

    /**
     * 예약 금액 (단위: 원)
     */
    @Column(nullable = false)
    private Long amount;

    /**
     * 예약 시각
     */
    @Column(nullable = false)
    private LocalDateTime reservedAt;

    /**
     * 청구 여부
     * <br/>월말 청구 완료 여부 (true = 청구됨, false = 청구 전)
     */
    @Column(nullable = false)
    private boolean charged;
}
