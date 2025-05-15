package com.fisa.card.controller;


import com.fisa.card.dto.req.PaymentRequest;
import com.fisa.card.dto.res.PaymentResultResponse;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import com.fisa.card.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fisa.card.dto.res.PaymentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    //private final JwtUtil jwtUtil;

    /**
     * PG사에서 카드사로 결제 승인을 요청하는 API
     * @param request PG사로부터 전달받은 결제 승인 요청 정보 (카드번호, 금액, 트랜잭션ID 등)
     * @return 결제 승인 처리 결과 (성공/실패 및 관련 정보)
     */
    @PostMapping("/authorization")
    @Operation(summary = "결제 승인 요청", description = """
            PG사에서 카드사로 결제 승인을 요청, 유효성검사 및 잔액 확인(은행서버), 결과 반환
            """)
    public ResponseEntity<ApiResponse<PaymentResultResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request
    ) {

        PaymentResultResponse response = paymentService.authorizePayment(request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }

    /**
     *  결제 승인 요청 조회하는 API.
     *
     * <p>
     *  Pg사가 카드사에 결제 요청한 내역 조회
     * </p>
     *
     * @return 결제 트랜잭션 리스트 응답 (상태, 금액, 카드정보 등 포함)
     */
    @GetMapping("/view")
    @Operation(summary = "전체 결제 요청 목록 조회", description = "결제 요청 전체 내역(PENDING, SUCCESS, FAILED )을 조회")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> allPayments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, allPayments));
    }

}
