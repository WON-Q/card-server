package com.fisa.card.account.domain;

import com.fisa.card.member.domain.Member;
import com.fisa.card.account.domain.enums.AccountStatus;
import com.fisa.card.card.domain.Card;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true, nullable = false, length = 30)
    private String accountNumber; // 계좌번호

    @Column(nullable = false)
    private Long balance; // 잔액


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    // Account → Member (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 양방향 관계: 하나의 계좌에 여러 카드
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Card> cards = new ArrayList<>();
}
