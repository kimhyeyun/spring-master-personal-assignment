package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.response.BaseResponse;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.TodoService;
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

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;


    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(SuccessCode.CREATED_TODO.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.CREATED_TODO, todoService.createTodo(requestDto, userDetails.getUser())));
    }

    @GetMapping
    public ResponseEntity<?> getTodoList() {
        return ResponseEntity.status(SuccessCode.SUCCESS_GET_TODO_LIST.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_GET_TODO_LIST, todoService.getTodoList())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyTodo(@PathVariable long id,
                                        @RequestBody @Valid TodoRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(SuccessCode.SUCCESS_UPDATE_TODO.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_UPDATE_TODO, todoService.modifyTodo(id, requestDto, userDetails.getUser()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        todoService.deleteTodo(id, userDetails.getUser());
        return ResponseEntity.status(SuccessCode.SUCCESS_DELETE_TODO.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_DELETE_TODO, null)
        );
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<?> finishedTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(SuccessCode.SUCCESS_FINISHED_TODO.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.SUCCESS_FINISHED_TODO, todoService.finishedTodo(id, userDetails.getUser()))
        );
    }
}
