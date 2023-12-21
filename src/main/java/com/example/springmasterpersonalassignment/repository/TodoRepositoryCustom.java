package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;

import java.util.List;

public interface TodoRepositoryCustom {
    List<Todo> searchTodoBy(String type, String keyword, User user);
}
