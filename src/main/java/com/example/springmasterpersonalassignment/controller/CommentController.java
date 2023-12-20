package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import com.example.springmasterpersonalassignment.dto.response.BaseResponse;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/{todoId}")
    public ResponseEntity<?> createComment(@PathVariable Long todoId,
                                           @RequestBody @Valid CommentRequest requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(SuccessCode.CREATED_COMMENT.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.CREATED_COMMENT, commentService.createComment(todoId, requestDto, userDetails.getUser()))
        );
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<?> getCommentListByTodoId(@PathVariable Long todoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(SuccessCode.SUCCESS_GET_TODO_LIST.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_GET_COMMENT_LIST, commentService.getCommentListByTodoId(todoId, userDetails.getUser()))
        );
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(SuccessCode.SUCCESS_UPDATE_COMMENT.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_UPDATE_COMMENT, commentService.modifyComment(commentId, requestDto, userDetails.getUser()))
        );
    }

    @GetMapping
    public ResponseEntity<?> getCommentListByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(SuccessCode.SUCCESS_GET_COMMENT_LIST_BY_USER.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_GET_COMMENT_LIST_BY_USER, commentService.getCommentListByUser())
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.status(SuccessCode.SUCCESS_DELETE_COMMENT.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_DELETE_COMMENT, null)
        );
    }
}
