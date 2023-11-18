package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
