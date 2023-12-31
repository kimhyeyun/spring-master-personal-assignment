package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import com.example.springmasterpersonalassignment.dto.response.CommentResponse;
import com.example.springmasterpersonalassignment.entity.Comment;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.CommentRepository;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("[CommentService Test]")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks private CommentServiceImpl sut;
    @Mock private CommentRepository commentRepository;
    @Mock private TodoRepository todoRepository;
    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 추가 시도 시, 실패")
    void givenNonExistedTodo_whenSave_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        CommentRequest requestDto = new CommentRequest("퍼가요~^^");

        // When && Then
        assertThatThrownBy(() -> sut.createComment(1L, requestDto, user))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_TODO.getMessage());
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void givenCommentRequest_whenSave_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        CommentRequest requestDto = new CommentRequest("퍼가요~^^");

        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // When
        CommentResponse result = sut.createComment(1L, requestDto, user);

        // Then
        assertEquals(result.content(), requestDto.content());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정 시, 실패")
    void givenNonExistedCommentId_whenModify_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        CommentRequest requestDto = new CommentRequest("퍼가요~^^");

        // When && Then
        assertThatThrownBy(() -> sut.modifyComment(1L, requestDto, user))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_COMMENT.getMessage());
    }

    @Test
    @DisplayName("작성자 아닌 사용자가 댓글 수정 시, 실패")
    void givenWrongUser_whenModify_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        Comment comment = createComment("나도 쓴다 TIL", todo, user);
        CommentRequest requestDto = new CommentRequest("퍼가요~^^");

        User other = createUser("whoareyou", "123456789");

        // When
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        // Then
        assertThrows(CustomException.class, () -> sut.modifyComment(1L, requestDto, other));
    }

    @Test
    @DisplayName("작성자가 댓글 수정 시, 성공")
    void givenCorrectUser_whenModify_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        Comment comment = createComment("나도 쓴다 TIL", todo, user);
        CommentRequest requestDto = new CommentRequest("퍼가요~^^");

        // When
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
        CommentResponse result = sut.modifyComment(1L, requestDto, user);

        // Then
        assertEquals(result.content(), requestDto.content());
    }

    @Test
    @DisplayName("작성자 아닌 사용자가 댓글 삭제 시, 실패")
    void givenWrongUser_whenDelete_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        Comment comment = createComment("나도 쓴다 TIL", todo, user);

        User other = createUser("whoareyou", "123456789");

        // When
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        // Then
        assertThrows(CustomException.class, () -> sut.deleteComment(1L, other));
    }

    @Test
    @DisplayName("작성자가 댓글 삭제 시, 성공")
    void givenCorrectUser_whenDelete_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        Comment comment = createComment("나도 쓴다 TIL", todo, user);

        // When
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        // Then
        assertThatCode(() -> sut.deleteComment(1L, user))
                .doesNotThrowAnyException();

    }

    private Comment createComment(String content, Todo todo, User user) {
        return Comment.builder()
                .content(content)
                .todo(todo)
                .user(user)
                .build();
    }

    private Todo createTodo(String title, String content, User user) {
        return Todo.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

    private User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
