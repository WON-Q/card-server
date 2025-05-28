package com.fisa.card.dto.res;

import com.fisa.card.entity.CardType;
import com.fisa.card.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(description = "결제 내역 조회 DTO")
public class PaymentResponse {

    @Schema(description = "트랜잭션 ID", example = "txnid98765")
    private String txnId;

    @Schema(description = "결제 상태", example = "SUCCESS")
    private PaymentStatus paymentStatus;;

    @Schema(description = "결제 금액", example = "50000")
    private Long amount;

    @Schema(description = "은행 청구 여부", example = "true")
    private boolean charged;

    @Schema(description = "카드 번호", example = "123456******3456")
    private String cardNumber;

    @Schema(description = "결제된 금액을 입금해줘야 하는 계좌번호 ", example = "123123123123")
    private String depositAccount;
}
