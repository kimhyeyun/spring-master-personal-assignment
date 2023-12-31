package com.example.springmasterpersonalassignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Todo> todoList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @Builder.Default
    private List<UserImage> images = new ArrayList<>();
}
