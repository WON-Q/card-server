package com.fisa.card.enitiy;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * 카드 정보를 관리하는 엔티티 클래스입니다.
 * <br/>
 * 각 카드는 고유한 카드번호를 가지며, 특정 계좌(Account)와 연결됩니다.
 */
@Entity
@Getter
@Table(name = "card")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Card {



    /**
     * 카드 번호 (PK, 고유값, Not Null)
     */
    @Id
    @Column(unique = true, nullable = false)
    private String cardNumber;

    /**
     * 카드 유형 (체크/신용 등)
     * EnumType.STRING으로 저장
     */
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    /**
     * 카드 BIN 번호 (Not Null)
     */
    @Column(nullable = false)
    private String cardBin;

    /**
     * 카드 만료일 (Not Null)
     */
    @Column(nullable = false)
    private LocalDate expiredAt;

    /**
     * 카드 CVC (Not Null)
     */
    @Column(nullable = false)
    private String cardCvc;

    /**
     * 카드 한도 금액 (Not Null)
     */
    @Column(nullable = false)
    private Long cardLimit;

    /**
     * 카드가 연결된 계좌 (ManyToOne 관계)
     */

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    /**
     * 카드 소유 회원 ID
     */
    private Long memberId; //
}
