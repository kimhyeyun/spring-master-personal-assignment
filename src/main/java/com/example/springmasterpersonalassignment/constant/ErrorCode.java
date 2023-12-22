package com.example.springmasterpersonalassignment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_VALUE(BAD_REQUEST, "값이 유효하지 않습니다."),
    INVALID_PARAMETER(BAD_REQUEST, "파라미터가 누락되었습니다."),
    INVALID_TYPE(BAD_REQUEST, "없는 검색 타입 입니다."),
    MAX_IMAGES_LIMIT_OVER(BAD_REQUEST, "등록할 이미지 개수가 최대 개수를 초과했습니다."),
    INVALID_POST_FILE_URL(BAD_REQUEST, "잘못된 이미지 URL 입니다."),
    CAN_NOT_READ_IMAGE(BAD_REQUEST, "이미지를 처리할 수 없습니다."),

    NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "없는 할 일 입니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "없는 댓글 입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "없는 유저 입니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    ALREADY_EXISTED_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 username 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러 입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
