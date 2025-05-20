package com.fisa.card.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankWithdrawRequest {

    @JsonProperty("account_number")
    @Schema(description = "출금 요청 계좌번호", example = "1234567890123456", required = true)
    private String account_number;


    @JsonProperty("amount")
    @Schema(description = "출금 요청 금액", example = "100000", required = true)
    private Long  amount;

}
