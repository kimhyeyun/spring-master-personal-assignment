package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
    Page searchTodoBy(String type, String keyword, User user, Pageable pageable);

}
