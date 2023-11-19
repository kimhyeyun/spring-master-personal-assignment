package com.example.springmasterpersonalassignment.dto.request;

import com.example.springmasterpersonalassignment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    @NotBlank private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
