package com.example.springmasterpersonalassignment.dto.request;

import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import jakarta.validation.constraints.NotBlank;

public record TodoRequest(
        @NotBlank(message = "필수로 입력해주세요.")
        String  title,

        @NotBlank(message = "필수로 입력해주세요.")
        String content

) {

    public Todo toEntity(User user) {
        return Todo.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

}
