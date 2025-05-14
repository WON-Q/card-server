package com.fisa.card.entity;

/**
 * 결제 처리 상태
 * <ul>
 *     <li>PENDING: 결제 요청이 접수되었으며, 아직 최종 결과가 확정되지 않은 상태</li>
 *     <li>SUCCESS: 결제가 정상적으로 승인 및 완료된 상태</li>
 *     <li>FAILED: 결제가 거절되거나 오류로 인해 실패한 상태</li>
 * </ul>
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
