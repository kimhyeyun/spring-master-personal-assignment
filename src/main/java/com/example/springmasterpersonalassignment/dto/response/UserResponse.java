package com.example.springmasterpersonalassignment.dto.response;

import com.example.springmasterpersonalassignment.entity.User;

import java.util.List;

public record UserResponse(
        String username,
        List<TodoResponse> todoList,
        List<CommentResponse> commentList
) {

    public static UserResponse of(User user) {
        return new UserResponse(user.getUsername(), user.getTodoList().stream().map(TodoResponse::of).toList(), user.getCommentList().stream().map(CommentResponse::of).toList());
    }
}
