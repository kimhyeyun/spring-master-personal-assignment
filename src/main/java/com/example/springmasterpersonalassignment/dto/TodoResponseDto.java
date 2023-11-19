package com.example.springmasterpersonalassignment.dto;

import com.example.springmasterpersonalassignment.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {

    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public static TodoResponseDto of(Todo todo) {
        return TodoResponseDto.builder()
                .title(todo.getTitle())
                .content(todo.getContent())
                .username(todo.getUser().getUsername())
                .createdAt(todo.getCreatedAt())
                .build();
    }
}
