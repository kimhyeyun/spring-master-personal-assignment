package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.dto.response.CommentResponseDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "UserController")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            List<String> msg = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                msg.add(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return userService.signup(requestDto);
    }


    // 확인을 위한 메서드 //
    @GetMapping("/todos")
    @ResponseBody
    public List<TodoResponseDto> getUserTodos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findTodoList(userDetails.getUser().getUsername());
    }

    @GetMapping("/comments")
    @ResponseBody
    public List<CommentResponseDto> getUserComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findCommentList(userDetails.getUser().getUsername());
    }
}
