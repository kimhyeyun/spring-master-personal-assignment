package com.example.springmasterpersonalassignment.dto.response;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.constant.SuccessCode;

public record BaseResponse <T> (
    String message,
    Integer statusCode,
    T data
) {

    public static <T> BaseResponse<T> of(String message, Integer statusCode, T data) {
        return new BaseResponse<>(message, statusCode, data);
    }

    public static <T> BaseResponse<T> of(ErrorCode errorResponse, T data) {
        return new BaseResponse<>(
                errorResponse.getMessage(),
                errorResponse.getHttpStatus().value(),
                data
        );
    }

    public static <T> BaseResponse<T> of(SuccessCode successCode, T data) {
        return new BaseResponse<>(
                successCode.getMessage(),
                successCode.getHttpStatus().value(),
                data
        );
    }
}
