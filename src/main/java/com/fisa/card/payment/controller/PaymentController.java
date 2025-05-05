package com.fisa.card.payment.controller;

import com.fisa.card.global.jwt.JwtUtil;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import com.fisa.card.payment.controller.dto.req.PaymentRequest;
import com.fisa.card.payment.controller.dto.res.PaymentResponse;
import com.fisa.card.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/card-issuer/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/authorize")
    @Operation(summary = "결제 승인 요청", description = """
            PG사에서 카드사로 결제 승인을 요청합니다.  
            - JWT 헤더를 통해 요청자인 PG사 인증  
            - 카드 유효성 및 한도 확인  
            - 결제 상태를 PENDING 으로 저장한 뒤 후처리 로직 분기
            """)
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpServletRequest
    ) {
        // 1. JWT 검증 (PG사 요청인지 확인)
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        jwtUtil.validateToken(token); // 유효성 검증

        // 서비스에서 모든 처리
        PaymentResponse response = paymentService.authorizePayment(token, request);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }
}
