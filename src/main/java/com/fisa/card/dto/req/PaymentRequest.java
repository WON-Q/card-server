package com.fisa.card.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "결제 승인 요청 DTO")
public class PaymentRequest {

    @NotNull
    @Schema(description = "PG사에서 생성한  트랜잭션ID", example = "txnid98765")
    private String txnId;

    @NotBlank
    @Schema(description = "결제된 금액을 입금해줘야 하는 계좌번호", example = "3333058911111")
    private String settlementAccountNumber;

    @NotNull
    @Schema(description = "결제 금액", example = "50000")
    private Long amount;

    @NotBlank
    @Schema(description = "카드 번호", example = "1234567890123456")
    private String cardNumber;


}
