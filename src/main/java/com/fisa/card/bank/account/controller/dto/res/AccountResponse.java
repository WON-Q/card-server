package com.fisa.card.bank.account.controller.dto.res;

import com.fisa.card.bank.account.domain.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponse {

    @Schema(description = "계좌 ID", example = "1")
    private Long accountId;

    @Schema(description = "계좌번호", example = "3333058919925")
    private String accountNumber;

    @Schema(description = "초기 잔액", example = "100000")
    private Long balance;

    @Schema(description = "계좌 상태", example = "ACTIVE")
    private AccountStatus status;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;
}
