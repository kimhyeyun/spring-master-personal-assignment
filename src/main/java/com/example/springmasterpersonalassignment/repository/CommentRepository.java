package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
