package com.fisa.card.payment.controller;

import com.fisa.card.global.jwt.JwtUtil;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import com.fisa.card.payment.controller.dto.req.PaymentRequest;
import com.fisa.card.payment.controller.dto.res.PaymentResponse;
import com.fisa.card.payment.controller.dto.res.PaymentResultResponse;
import com.fisa.card.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;


    /**
     * PG사에서 카드사로 결제 승인을 요청하는 API
     *
     * <p>
     * 카드사의 결제 승인 흐름은 다음과 같은 단계를 거칩니다:
     * <ol>
     *     <li><b>1단계:</b> PG사에서 카드사로 결제 승인 요청 (현재 이 API)</li>
     *     <li><b>2단계:</b> 카드 유효성 검증 (만료일 확인)</li>
     *     <li><b>3단계:</b> 카드 한도 검사</li>
     *     <li><b>4단계:</b> 결제 정보 PENDING 상태로 저장</li>
     *     <li><b>5단계:</b> 카드 타입에 따라 분기 처리
     *         <ul>
     *             <li>체크카드: 은행 서버로 잔액 확인 및 출금 요청</li>
     *             <li>신용카드: 별도 출금 요청 없이 성공 처리</li>
     *         </ul>
     *     </li>
     *     <li><b>6단계:</b> 결제 상태 SUCCESS / FAILED로 업데이트</li>
     *     <li><b>7단계:</b> PG사에 최종 승인 결과 응답</li>
     * </ol>
     * </p>
     *
     * <p>
     * 자세한 내용은 프로젝트 내 {@code docs/payment-flow.md} 문서를 참조해 주세요.
     * </p>
     *
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
        // 1. JWT 검증 (PG사 요청인지 확인)
//        String authHeader = httpServletRequest.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new SecurityException("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7); // "Bearer " 제거
//        jwtUtil.validateToken(token); // 유효성 검증

        PaymentResultResponse response = paymentService.authorizePayment(request);

        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, response));
    }


    /**
     * 완료된 결제(성공 또는 실패) 내역을 조회하는 API입니다.
     *
     * <p>
     * 결제 승인 처리 이후의 모든 트랜잭션(PENDING 제외)을 조회하며,
     * 상태는 {@code SUCCESS}, {@code FAILED} 두 가지가 포함됩니다.
     * </p>
     *
     * @return 결제 트랜잭션 리스트 응답 (상태, 금액, 카드정보 등 포함)
     */
    @GetMapping("/transactions")
    @Operation(summary = "결제 요청 목록 조회", description = "결제(실패,성공) 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getCompletedPayments() {
        List<PaymentResponse> approvedPayments = paymentService.getCompletedPayments();
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, approvedPayments));
    }

}
