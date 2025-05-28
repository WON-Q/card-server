package com.fisa.card.global.response;



import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    SUCCESS("S200", "요청이 성공적으로 처리되었습니다.");

    private final String code;
    private final String message;

}
