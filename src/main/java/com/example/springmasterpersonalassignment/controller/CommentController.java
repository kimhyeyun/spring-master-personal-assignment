package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.dto.request.CommentRequestDto;
import com.example.springmasterpersonalassignment.dto.response.CommentResponseDto;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.CommentService;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "CommentController")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/{todoId}")
    @ResponseBody
    public ResponseEntity<?> createComment(@PathVariable Long todoId,
                                           @RequestBody @Valid CommentRequestDto requestDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력해주세요");
        }

        return commentService.createComment(todoId, requestDto, userDetails.getUser());
    }

    @GetMapping("/{todoId}")
    public List<CommentResponseDto> getCommentListByTodoId(@PathVariable Long todoId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getCommentListByTodoId(todoId, userDetails.getUser());
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.modifyComment(commentId, requestDto, userDetails.getUser());
    }
}
