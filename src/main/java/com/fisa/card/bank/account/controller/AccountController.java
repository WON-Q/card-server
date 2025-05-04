package com.fisa.card.bank.account.controller;


import com.fisa.card.bank.account.controller.dto.req.AccountRequest;
import com.fisa.card.bank.account.controller.dto.res.AccountResponse;
import com.fisa.card.bank.account.service.AccountService;
import com.fisa.card.global.response.ApiResponse;
import com.fisa.card.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/create/{memberId}")
    @Operation(summary = "계좌 생성", description = "회원 ID와 계좌 정보를 통해 계좌를 생성합니다.")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @PathVariable Long memberId,
            @Valid @RequestBody AccountRequest request
    ) {
        AccountResponse resp = accountService.createAccount(memberId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, resp));
    }

}
