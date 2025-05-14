package com.fisa.card.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    private ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 기본 성공 응답
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    // 커스텀 응답 코드와 함께
    public static <T> ApiResponse<T> of(ResponseCode code, T data) {
        return new ApiResponse<>(code.getCode(), code.getMessage(), data);
    }

    // 데이터 없이 응답 코드만
    public static <T> ApiResponse<T> of(ResponseCode code) {
        return new ApiResponse<>(code.getCode(), code.getMessage(), null);
    }

}
