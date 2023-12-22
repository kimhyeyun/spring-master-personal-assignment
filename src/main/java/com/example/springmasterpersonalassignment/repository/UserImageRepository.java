package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    List<UserImage> findAllByUser_Username(String username);
}
