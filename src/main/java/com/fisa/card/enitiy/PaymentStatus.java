package com.fisa.card.enitiy;

/**
 * 결제 처리 상태를 나타내는 열거형(Enum)입니다.
 * <p>
 * 결제가 진행 중인지, 성공적으로 완료되었는지, 또는 실패했는지를 구분하기 위해 사용됩니다.
 */
public enum PaymentStatus {

    /**
     * 결제 요청이 접수되었으며, 아직 최종 결과가 확정되지 않은 상태
     */
    PENDING,

    /**
     * 결제가 정상적으로 승인 및 완료된 상태
     */
    SUCCESS,

    /**
     * 결제가 거절되거나 오류로 인해 실패한 상태
     */
    FAILED,
}
