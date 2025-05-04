package com.fisa.card.card.controller;

import com.fisa.card.card.controller.dto.req.CardRequest;
import com.fisa.card.card.controller.dto.res.CardResponse;
import com.fisa.card.card.service.CardService;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/{memberId}/cards")
    @Operation(summary = "카드 생성", description = "계좌번호를 기준으로 카드를 생성합니다.")
    public ResponseEntity<ApiResponse<CardResponse>> createCard(
            @PathVariable Long memberId,
            @Valid @RequestBody CardRequest request
    ) {
        CardResponse response = cardService.createCard(memberId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }
}

