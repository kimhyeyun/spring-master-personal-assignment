package com.example.springmasterpersonalassignment.dto.response;

import com.example.springmasterpersonalassignment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String username,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {


    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getCreatedAt(), comment.getModifiedAt());
    }
}
