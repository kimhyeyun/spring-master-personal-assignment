package com.example.springmasterpersonalassignment.dto.response;

import com.example.springmasterpersonalassignment.entity.Todo;

import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String title,
        String content,
        String username,
        boolean isFinished,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static TodoResponse of(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getTitle(), todo.getContent(), todo.getUser().getUsername(), todo.isFinished(), todo.getCreatedAt(), todo.getModifiedAt());
    }

}
