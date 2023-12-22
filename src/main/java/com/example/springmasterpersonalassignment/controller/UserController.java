package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.DeleteImageRequest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequest;
import com.example.springmasterpersonalassignment.dto.response.BaseResponse;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest requestDto) {
        UserResponse response = userService.signup(requestDto);
        return ResponseEntity.status(SuccessCode.CREATED_USER.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.CREATED_USER, response)
        );
    }

    @PostMapping("/{username}/images")
    public ResponseEntity<?> uploadImages(
            @PathVariable String username,
            @RequestParam("images") MultipartFile[] images,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserResponse response = userService.uploadImage(username, images, userDetails.getUser());
        return ResponseEntity.status(SuccessCode.CREATED_USER_IMAGE.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.CREATED_USER_IMAGE, response)
        );
    }

    @DeleteMapping("/{username}/images")
    public ResponseEntity<?> deleteImages(
            @PathVariable String username,
            @RequestBody DeleteImageRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UserResponse response = userService.deleteImage(username, request, userDetails.getUser());
        return ResponseEntity.status(SuccessCode.DELETE_USER_IMAGE.getHttpStatus()).body(
                BaseResponse.of(SuccessCode.DELETE_USER_IMAGE, response));
    }
}
