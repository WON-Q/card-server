package com.fisa.card.enitiy;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  회원(카드,계좌 소유한) 엔티티 클래스입니다.
 *  계좌,카드 소유주를 파악하기 위한 간단한 엔티티
 *
 * <br/>
 * 하나의 회원은 여러 개의 계좌(Account)를 가질 수 있다.
 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 회원 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    /**
     * 회원 이름
     */
    @Column(nullable = false, length = 50)
    private String memberName;

    /**
     * 회원이 보유한 계좌
     * <br/>
     * Member(1) ↔ Account(N) 관계를 나타내며,
     * Account 엔티티의 'member' 필드에 의해 매핑됩니다.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}
