package com.fisa.card.enitiy;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 계좌 정보를 저장하는 엔티티 클래스입니다.
 * <p>
 * 각 계좌는 고유한 계좌번호를 가지며, 잔액과 상태를 포함합니다.
 * 하나의 계좌는 여러 개의 카드와 연결될 수 있으며,
 * 하나의 회원(Member)은 여러 개의 계좌를 가질 수 있습니다.
 */
@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    /**
     * 계좌 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    /**
     * 고유한 계좌번호
     * <br/> 최대 길이 30, 중복 불가, Not Null
     */
    @Column(unique = true, nullable = false, length = 30)
    private String accountNumber;

    /**
     * 계좌 잔액 (원화 기준)
     * <br/> Not Null
     */
    @Column(nullable = false)
    private Long balance;

    /**
     * 계좌 상태 (활성, 정지 등)
     * <br/> EnumType.STRING으로 저장, Not Null
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    /**
     * 계좌 소유 회원 (N:1 관계)
     * <br/> 각 계좌는 반드시 하나의 회원에 연결되어야 함 (nullable = false)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 해당 계좌에 연결된 카드 목록 (1:N 관계)
     * <br/> Account가 삭제되면 연결된 카드도 함께 삭제됨 (
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Card> cards = new ArrayList<>();
}
