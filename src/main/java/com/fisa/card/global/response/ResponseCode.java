package com.fisa.card.global.response;



import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // ✅ HTTP 200 OK
    SUCCESS("S001", "요청이 성공적으로 처리되었습니다.");

    private final String code;
    private final String message;
}
