package com.fisa.card.dto.req;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankDepositRequest {


    @Schema(description = "입금 요청 계좌번호", example = "1234567890123456", required = true)
    private String accountNumber;

    @Schema(description = "입금 요청 금액", example = "100000", required = true)
    private Long amount;

}
