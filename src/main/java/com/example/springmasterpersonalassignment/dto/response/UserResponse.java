package com.example.springmasterpersonalassignment.dto.response;

import com.example.springmasterpersonalassignment.entity.User;

import java.util.List;

public record UserResponse(
        String username,
        List<TodoResponseDto> todoList,
        List<CommentResponseDto> commentList
) {

    public static UserResponse of(User user) {
        return new UserResponse(user.getUsername(), user.getTodoList().stream().map(TodoResponseDto::of).toList(), user.getCommentList().stream().map(CommentResponseDto::of).toList());
    }
}
