package com.fisa.card.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 카드 정보를 관리하는 엔티티
 * <br/>
 * 각 카드는 고유한 카드번호를 가지며, 특정 계좌와 연결된다.
 */
@Table(name = "card")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    /**
     * 카드 번호 (PK, 고유값, Not Null)
     */
    @Id
    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    /**
     * 카드 만료일
     */
    @Column(name = "expired_at", nullable = false)
    private LocalDate expiredAt;

    /**
     * 카드 CVC
     */
    @Column(name = "card_cvc", nullable = false)
    private String cardCvc;

    /**
     * 신용카드 한도 금액
     */
    @Column(name = "card_limit", nullable = true)
    private Long cardLimit;

    /**
     * 카드가 연결된 계좌
     */
    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    /**
     * 카드 소유 회원 ID
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
