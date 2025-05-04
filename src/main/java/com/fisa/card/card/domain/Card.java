package com.fisa.card.card.domain;

import com.fisa.card.bank.account.domain.Account;
import com.fisa.card.card.domain.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/*
  카드 테이블
 */
@Entity
@Getter
@Table(name="card")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(unique = true,nullable = false)
    private  String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(nullable = false)
    private Integer cardBIN;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private Long memberId;

}
