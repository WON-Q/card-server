package com.fisa.card.dto.res;

import com.fisa.card.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    @Schema(description = "PG사에서 요청한 트랜잭션 ID", example = "txnid98765")
    private String txnId;

    @Schema(description = "결제 상태 (CANCELLED 또는 FAILED)", example = "CANCELLED")
    private PaymentStatus paymentStatus; // CANCELLED or FAILED

}
