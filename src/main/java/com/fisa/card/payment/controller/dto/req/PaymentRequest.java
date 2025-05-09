package com.fisa.card.payment.controller.dto.req;

import com.fisa.card.card.domain.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Schema(description = "결제 승인 요청 DTO")
public class PaymentRequest {

    @NotNull
    @Schema(description = "PG사에서 생성한  트랜잭션ID", example = "txnid98765")
    private String txnId;

    @NotBlank
    @Schema(description = "가맹점 이름", example = "교촌치킨")
    private String merchantname;

    @NotNull
    @Schema(description = "결제 금액", example = "50000")
    private Long amount;

    @NotBlank
    @Schema(description = "카드 번호", example = "1234567890123456")
    private String cardNumber;


}
