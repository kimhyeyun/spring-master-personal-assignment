package com.example.springmasterpersonalassignment.dto;

import com.example.springmasterpersonalassignment.entity.Todo;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {

    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static TodoResponseDto of(Todo todo) {
        return TodoResponseDto.builder()
                .title(todo.getTitle())
                .content(todo.getContent())
                .username(todo.getUser().getUsername())
                .createdAt(todo.getCreatedAt())
                .modifiedAt(todo.getModifiedAt())
                .build();
    }
}
