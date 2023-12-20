package com.example.springmasterpersonalassignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    @Size(min = 4, max = 10, message = "최소 4자, 최대 10자로 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "영어 소문자와 숫자로만 입력해주세요.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 15, message = "최소 8자, 최대 15자로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "영어와 숫자로만 입력해주세요.")
    private String password;
}
