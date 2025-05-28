package com.fisa.card.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "은행 입금 응답 DTO")
public class BankDepositResponse {

    @Schema(description = "입금된 계좌번호", example = "1005-001-123456")
    private String account;

    @Schema(description = "입금된 금액", example = "50000")
    private Long amount;

    @Schema(description = "입금 처리 상태", example = "SUCCESS")
    private String status;

    @Schema(description = "잔여 계좌 잔액", example = "150000")
    private Long balance;
}
