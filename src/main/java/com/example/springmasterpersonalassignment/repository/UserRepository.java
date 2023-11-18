package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<String, User> {
}
