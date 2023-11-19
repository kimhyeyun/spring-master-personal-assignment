package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.dto.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.TodoResponseDto;
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

@Slf4j(topic = "TodoController")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @ResponseBody
    @PostMapping()
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoRequestDto requestDto,
                                                      BindingResult bindingResult,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모두 입력해주세요.");
        }

        return todoService.createTodo(requestDto, userDetails.getUser());
    }

    @GetMapping()
    public List<TodoResponseDto> getTodoList() {
        return todoService.getTodoList();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyTodo(@PathVariable long id,
                                        @RequestBody @Valid TodoRequestDto requestDto,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoService.modifyTodo(id, requestDto, userDetails.getUser());
    }
}
