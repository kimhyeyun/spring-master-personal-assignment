package com.example.springmasterpersonalassignment.exception;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();

    }
}
