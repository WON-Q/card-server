package com.fisa.card.bank.account.controller.dto.req;


import com.fisa.card.bank.account.domain.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "계좌 생성 요청 DTO")
public class AccountRequest {

    @Schema(description = "계좌번호", example = "3333058919925")
    private String accountNumber;

    @Schema(description = "초기 잔액", example = "100000")
    private Long balance;

    @Schema(description = "계좌 상태", example = "ACTIVE")
    private AccountStatus status;
}
