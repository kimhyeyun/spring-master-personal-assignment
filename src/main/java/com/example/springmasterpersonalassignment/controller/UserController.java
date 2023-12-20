package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.dto.response.BaseResponse;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;
import com.example.springmasterpersonalassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        UserResponse response = userService.signup(requestDto);
        return ResponseEntity.status(SuccessCode.CREATED_USER.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.CREATED_USER, response)
        );
    }

}
