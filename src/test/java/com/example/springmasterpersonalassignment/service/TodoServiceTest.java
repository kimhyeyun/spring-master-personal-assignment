package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.TodoRequest;
import com.example.springmasterpersonalassignment.dto.response.TodoResponse;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("[TodoService 테스트]")
class TodoServiceTest {

    @InjectMocks private TodoServiceImpl sut;
    @Mock private TodoRepository todoRepository;
    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("할 일 추가 성공 테스트")
    void givenNewTodo_whenSaveTodo_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        TodoRequest requestDto = new TodoRequest("test title", "test content");

        // When
        TodoResponse result = sut.createTodo(requestDto, user);

        // Then
        assertEquals(result.username(), user.getUsername());
        assertEquals(result.title(), requestDto.title());
    }

    @Test
    @DisplayName("작성자가 아닌 다른 사용자가 수정 시도 시, 실패")
    void givenOtherUser_whenModifyTodo_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        User otherUser = createUser("noowner", "123456789");
        TodoRequest requestDto = new TodoRequest("과제끝내", "오늘까지");

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // Then
        assertThatThrownBy(() -> sut.modifyTodo(1L, requestDto, otherUser))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCESS_DENIED.getMessage());
        assertNotEquals(todo.getTitle(), requestDto.title());
    }

    @Test
    @DisplayName("본인이 작성한 할 일 수정 시도 시, 성공")
    void givenCorrectUser_whenModifyTodo_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        TodoRequest requestDto = new TodoRequest("과제 끝내", "오늘까지");

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));
        TodoResponse result = sut.modifyTodo(1L, requestDto, user);

        // Then
        assertEquals(result.title(), requestDto.title());
        assertEquals(result.content(), requestDto.content());
        assertEquals(result.username(), user.getUsername());
    }

    @Test
    @DisplayName("존재하지 않은 할 일 번호로 수정 시도 시, 실성")
    void givenNonExistedTodoId_whenModifyTodo_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        TodoRequest requestDto = new TodoRequest("과제 끝내", "오늘까지");

        // When && Then
        assertThatThrownBy(() -> sut.modifyTodo(1L, requestDto, user))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_TODO.getMessage());
    }

    @Test
    @DisplayName("작성하지 않은 사용자가 삭제 요청 시, 실패")
    void givenOtherUser_whenDelete_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        User otherUser = createUser("noowner", "123456789");

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // Then
        assertThatThrownBy(() -> sut.deleteTodo(1L, otherUser))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCESS_DENIED.getMessage());
    }

    @Test
    @DisplayName("사용자가 삭제 요청 시, 성공")
    void givenCorrectUser_whenDelete_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // Then
        assertThatCode(() -> sut.deleteTodo(1L, user))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("작성하지 않은 사용자가 완료 처리 요청 시, 실패")
    void givenOtherUser_whenTryFinished_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        User otherUser = createUser("noowner", "123456789");

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // Then
        assertThrows(CustomException.class, () -> sut.finishedTodo(1L, otherUser));
    }

    @Test
    @DisplayName("작성자가 완료 처리 요청 시, 성공")
    void givenCorrectUser_whenTryFinished_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));
        TodoResponse result = sut.finishedTodo(1L, user);

        // Then
        assertEquals(result.title(), todo.getTitle());
        assertEquals(result.content(), todo.getContent());
        assertEquals(result.isFinished(), true);
    }

    private Todo createTodo(User user) {
        return Todo.builder()
                .title("TIL")
                .content("적어볼까")
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
