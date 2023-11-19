package com.example.springmasterpersonalassignment.dto;

import com.example.springmasterpersonalassignment.entity.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequestDto {

    @NotBlank private String title;
    @NotBlank private String content;

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .build();
    }
}
