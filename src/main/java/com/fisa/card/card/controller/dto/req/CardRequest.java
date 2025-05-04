package com.fisa.card.card.controller.dto.req;

import com.fisa.card.card.domain.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카드 생성 요청 DTO")
public class CardRequest {

    @Schema(description = "카드번호", example = "1234-5678-9012-3456")
    private String cardNumber;

    @Schema(description = "카드 타입", example = "CREDIT")
    private CardType cardType;

    @Schema(description = "카드 BIN", example = "123456")
    private Integer cardBIN;

    @Schema(description = "만료일", example = "2028-12-31")
    private String expiredAt;

    @Schema(description = "CVV", example = "123")
    private String cvv;

    @Schema(description = "계좌번호", example = "3333058919925")
    private String accountNumber;


}
