package com.fisa.card.card.controller;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import com.fisa.card.card.service.OpaqueTokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OpaqueController {

    private final OpaqueTokenService opaqueTokenService;

    @PostMapping("/bank/token")
    @Operation(summary = "은행 인증용 Opaque 토큰 요청", description = "카드사 인증을 위한 Opaque 토큰을 발급받습니다.")
    public ResponseEntity<ApiResponse<Map<String, String>>> issueOpaqueAuthToken(
            @RequestBody Map<String, String> request
    ) {
        String clientId = request.get("clientId");
        String clientSecret = request.get("clientSecret");

        String opaqueToken = opaqueTokenService.issueOpaqueAuthToken(clientId, clientSecret);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, Map.of("authToken", opaqueToken)));
    }
}
