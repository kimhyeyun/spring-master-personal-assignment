package com.example.springmasterpersonalassignment.dto.request;

import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
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

    public Todo toEntity(User user) {
        return Todo.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

}
