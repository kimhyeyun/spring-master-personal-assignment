package com.example.springmasterpersonalassignment.dto.request;

import com.example.springmasterpersonalassignment.entity.Comment;
import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "필수로 입력해주세요.")
        String content
){

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}

