package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.entity.Comment;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class JpaRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;
    @Autowired private CommentRepository commentRepository;

    @Test
    @DisplayName("새로운 User 저장 성공")
    void create_new_user_success() {
        // Given
        User user = generateUser();

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    @DisplayName("새로운 할일 저장 성공")
    void create_new_todo_success() {
        // Given
        User user = generateUser();
        Todo todo = generateTodo(user);

        // When
        userRepository.save(user);
        Todo savedTodo = todoRepository.save(todo);

        // Then
        assertEquals(todo.getUser().getUsername(), savedTodo.getUser().getUsername());
        assertEquals(todo.getTitle(), savedTodo.getTitle());
        assertEquals(todo.getContent(), savedTodo.getContent());
    }

    @Test
    @DisplayName("새로운 댓글 저장 성공")
    void create_new_comment_success() {
        // Given
        User user = generateUser();
        Todo todo = generateTodo(user);
        Comment comment = generateComment(user, todo);

        // When
        userRepository.save(user);
        todoRepository.save(todo);
        Comment savedComment = commentRepository.save(comment);

        // Then
        assertEquals(savedComment.getContent(), comment.getContent());
        assertEquals(savedComment.getUser().getUsername(), user.getUsername());
        assertEquals(savedComment.getTodo().getContent(), todo.getContent());

    }

    private Comment generateComment(User user, Todo todo) {
        return Comment.builder()
                .content("test comment")
                .user(user)
                .todo(todo)
                .build();
    }

    private Todo generateTodo(User user) {
        return Todo.builder()
                .title("test todo")
                .content("test content")
                .user(user)
                .build();
    }

    private User generateUser() {
        return User.builder()
                .username("tester")
                .password("123456789")
                .build();
    }
}