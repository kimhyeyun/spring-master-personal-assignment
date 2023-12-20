package com.example.springmasterpersonalassignment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "필수로 입력해주세요.")
        String username,
        @NotBlank(message = "필수로 입력해주세요.")
        String password
) {

}
