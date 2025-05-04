package com.fisa.card.card.controller.dto.res;


import com.fisa.card.card.domain.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardResponse {
    @Schema(description = "카드 ID")
    private Long cardId;
    @Schema(description = "카드번호")
    private String cardNumber;
    @Schema(description = "카드 타입")
    private CardType cardType;
    @Schema(description = "카드 BIN")
    private Integer cardBIN;
    @Schema(description = "만료일")
    private String expiredAt;
    @Schema(description = "CVV")
    private String cvv;
}
