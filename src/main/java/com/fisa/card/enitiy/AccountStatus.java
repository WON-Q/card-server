package com.fisa.card.enitiy;

/**
 * 계좌 상태를 나타내는 열거형(Enum)입니다.
 * <p>
 * 계좌의 사용 가능 여부 및 운영 상태를 나타내며,
 */
public enum AccountStatus {

    /**
     * 정상적으로 사용 가능한 활성 상태
     */
    ACTIVE,

    /**
     * 일시적으로 정지된 상태
     */
    SUSPENDED,

    /**
     * 영구적으로 해지된 상태
     */
    CLOSED
}
