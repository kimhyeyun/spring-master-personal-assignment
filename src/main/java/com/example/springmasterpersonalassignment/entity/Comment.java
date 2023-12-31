package com.example.springmasterpersonalassignment.entity;

import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    public void modify(CommentRequest requestDto) {
        this.content = requestDto.content();
    }
}
