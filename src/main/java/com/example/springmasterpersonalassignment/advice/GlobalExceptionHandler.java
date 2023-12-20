package com.example.springmasterpersonalassignment.advice;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.response.BaseResponse;
import com.example.springmasterpersonalassignment.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * [RuntimeException]
     *
     * @param RuntimeException
     * @return ResponseEntity<BaseResponse>
     * */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException exception) {
        log.error("RuntimeException: ", exception);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(
                BaseResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, null)
        );
    }

    /*
    * [IllegalArgumentException]
    *
    * @param IllegalArgumentException
    * @return ResponseEntity<BaseResponse>
    */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentHandler(IllegalArgumentException exception) {
        log.error("IllegalArgumentException: ", exception);
        return ResponseEntity.status(ErrorCode.INVALID_VALUE.getHttpStatus()).body(
                BaseResponse.of(ErrorCode.INVALID_VALUE, null)
        );
    }

    /*
     * [MethodArgumentNotValidException]
     *
     * @param  MethodArgumentNotValidException,
     * @return ResponseEntity<BaseResponse>
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidHandler(MethodArgumentNotValidException exception){
        log.error("handleMethodArgumentNotValidException: ", exception);
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        return ResponseEntity.status(ErrorCode.INVALID_VALUE.getHttpStatus()).body(
                BaseResponse.of(ErrorCode.INVALID_VALUE, errors)
        );
    }


    /*
     * [CustomException]
     *
     * @param CustomException
     * @return ResponseEntity<BaseResponse>
     * */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException exception) {
        log.error("CustomException: ", exception);
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(
                BaseResponse.of(exception.getErrorCode(), null)
        );
    }
}
