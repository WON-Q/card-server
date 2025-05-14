package com.fisa.card.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 정보를 담는 회원 클래스
 */
@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    /**
     * 회원 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 이름
     */
    @Column(nullable = false, length = 50)
    private String name;

}
