package com.fisa.card.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "은행 출금 응답 DTO")
public class BankWithdrawResponse {

    @Schema(description = "출금된 계좌번호", example = "1111222233334444")
    private String account;

    @Schema(description = "출금된 금액", example = "50000")
    private Long amount;

    @Schema(description = "출금 처리 상태", example = "SUCCESS")
    private String status;

    @Schema(description = "잔여 계좌 잔액", example = "150000")
    private Long balance;
}
