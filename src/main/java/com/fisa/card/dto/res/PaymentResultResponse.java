package com.fisa.card.dto.res;

import com.fisa.card.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "결제 결과 응답 DTO")
public class PaymentResultResponse {

    @Schema(description = "트랜잭션 ID", example = "txnid98765")
    private String txnId;

    @Schema(description = "결제 상태", example = "SUCCESS")
    private PaymentStatus paymentStatus;
}
