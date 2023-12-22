package com.example.springmasterpersonalassignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    private String imageUrl;

    public UserImage(User user, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;
        user.getImages().add(this);
    }
}
