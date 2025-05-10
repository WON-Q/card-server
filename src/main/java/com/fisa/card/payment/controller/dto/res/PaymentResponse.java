package com.fisa.card.payment.controller.dto.res;

import com.fisa.card.card.domain.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 내역 조회 DTO")
public class PaymentResponse {

    @Schema(description = "트랜잭션 ID", example = "txnid98765")
    private String txnId;

    @Schema(description = "결제 상태", example = "SUCCESS")
    private String status;

    @Schema(description = "결제 금액", example = "50000")
    private Long amount;

    @Schema(description = "카드 타입", example = "CREDIT")
    private CardType cardType;

    @Schema(description = "카드 번호", example = "123456******3456")
    private String cardNumber;

    @Schema(description = "가맹점 이름", example = "원큐카페 홍대점")
    private String merchant;
}
