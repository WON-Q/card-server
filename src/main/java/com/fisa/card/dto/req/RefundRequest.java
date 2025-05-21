package com.fisa.card.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "환불 요청 DTO")
public class RefundRequest {

    @Schema(description = "PG사에서 발급한 트랜잭션 ID", example = "txnid98765")
    private String txnId;

}
