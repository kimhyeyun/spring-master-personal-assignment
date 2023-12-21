package com.example.springmasterpersonalassignment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    CREATED_USER(HttpStatus.CREATED, "회원 가입에 성공했습니다."),
    CREATED_TODO(HttpStatus.CREATED, "할 일 작성에 성공했습니다."),
    CREATED_COMMENT(HttpStatus.CREATED, "댓글 작성에 성공했습니다."),

    SUCCESS_GET_TODO_LIST(HttpStatus.OK, "할 일 목록 조회에 성공했습니다"),
    SUCCESS_UPDATE_TODO(HttpStatus.OK, "할 일 수정에 성공했습니다"),
    SUCCESS_DELETE_TODO(HttpStatus.OK, "할 일 삭제에 성공했습니다"),
    SUCCESS_FINISHED_TODO(HttpStatus.OK, "할 일 완료 처리에 성공했습니다"),
    SUCCESS_SEARCH_TODO(HttpStatus.OK, "할 일 검색에 성공했습니다"),

    SUCCESS_GET_COMMENT_LIST(HttpStatus.OK, "댓글 목록 조회에 성공했습니다"),
    SUCCESS_UPDATE_COMMENT(HttpStatus.OK, "댓글 수정에 성공했습니다"),
    SUCCESS_DELETE_COMMENT(HttpStatus.OK, "댓글 삭제에 성공했습니다"),
    SUCCESS_GET_COMMENT_LIST_BY_USER(HttpStatus.OK, "유저 별 댓글 조회에 성공했습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
