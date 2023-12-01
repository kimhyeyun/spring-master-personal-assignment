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

import java.util.List;
import java.util.Optional;

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
    @DisplayName("새로운 댓글 저장 성공  ")
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

    @Test
    @DisplayName("저장된 여러 유저들 중, 유저 이름으로 유저 찾기 테스트")
    void userFindByUsername() {
        // Given
        User user = generateUser();
        userRepository.saveAndFlush(user);
        for (int i = 0; i < 10; i++) {
            createUser("username" + i, "testPassword" + i);
        }
        String findUsername = user.getUsername();

        // When
        Optional<User> findUser = userRepository.findByUsername(findUsername);

        // Then
        assertEquals(findUser.get().getUsername(), user.getUsername());
        assertEquals(findUser.get().getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("특정 유저의 할 일 목록을 생성일자 내림차순으로 조회")
    void todoFindAllByUserOrderByCreatedAtByDesc() {
        // Given
        for (int i = 0; i < 3; i++) {
            User user = createUser("username" + i, "testPassword" + i);
            for (int j = 0; j < 5; j++) {
                createTodo("todo" + i + j, "content" + i + j, user);
            }
        }

        Optional<User> findUser = userRepository.findByUsername("username2");

        // When
        List<Todo> result = todoRepository.findAllByUserOrderByCreatedAtDesc(findUser.get());

        // Then
        for (Todo todo : result) {
            System.out.println(todo.getTitle());
        }
        assertThat(result).hasSize(5)
                .map(Todo::getTitle)
                .contains("todo20", "todo21", "todo22", "todo23", "todo24");
    }

    @Test
    @DisplayName("특정 사용자의 댓글 목록 조회")
    void commentFindAllByUser() {
        // Given
        for (int i = 0; i < 2; i++) {
            User user = createUser("username" + i, "testPassword" + i);
            for (int j = 0; j < 3; j++) {
                Todo todo = createTodo("todo" + i + j, "content" + i + j, user);
                for (int k = 0; k < 5; k++) {
                    createComment("comment" + i + j + k, todo, user);
                }
            }
        }

        Optional<User> findUser = userRepository.findByUsername("username0");

        // When
        List<Comment> result = commentRepository.findAllByUser(findUser.get());

        // Then
        assertThat(result).hasSize(15)
                .map(Comment::getContent)
                .contains("comment000", "comment001", "comment002", "comment003", "comment004",
                        "comment010", "comment011", "comment012", "comment013", "comment014",
                        "comment020", "comment021", "comment022", "comment023", "comment024");

    }

    private void createComment(String content, Todo todo, User user) {
        commentRepository.saveAndFlush(Comment.builder()
                .content(content)
                .todo(todo)
                .user(user)
                .build());
    }

    private Todo createTodo(String title, String content, User user) {
        return todoRepository.saveAndFlush(Todo.builder()
                .title(title)
                .content(content)
                .user(user)
                .build());
    }

    private User createUser(String username, String password) {
        return userRepository.saveAndFlush(User.builder()
                .username(username)
                .password(password)
                .build());
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