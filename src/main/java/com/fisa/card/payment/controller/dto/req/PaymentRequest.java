package com.fisa.card.payment.controller.dto.req;

import com.fisa.card.card.domain.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "결제 승인 요청 DTO")
public class PaymentRequest {

    @NotNull
    @Schema(description = "PG사에서 생성한 결제 ID", example = "98765")
    private Long paymentId;

    @NotBlank
    @Schema(description = "가맹점 이름", example = "교촌치킨")
    private String merchantname;

    @NotNull
    @Schema(description = "결제 금액", example = "50000")
    private Long amount;

    @NotBlank
    @Schema(description = "카드 번호", example = "1234567890123456")
    private String cardNumber;

    @NotBlank
    @Schema(description = "카드 만료일 (MM/YY)", example = "12/27")
    private String expiryDate;

    @NotBlank
    @Schema(description = "CVV 번호", example = "123")
    private String cvv;

    @NotNull
    @Schema(description = "카드 타입", example = "CREDIT")
    private CardType cardType;

    @NotNull
    @Schema(description = "요청 일시", example = "2025-05-05T15:00:00")
    private LocalDateTime requestedAt;
}
