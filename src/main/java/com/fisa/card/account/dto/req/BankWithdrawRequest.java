package com.fisa.card.account.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankWithdrawRequest {

    @Schema(description = "출금 요청 계좌번호", example = "1234567890123456", required = true)
    private String account;

    @Schema(description = "출금 요청 금액", example = "100000", required = true)
    private Long amount;
}
