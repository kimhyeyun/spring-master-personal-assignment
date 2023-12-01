package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("[TodoService 테스트]")
class TodoServiceTest {

    @InjectMocks private TodoService sut;
    @Mock private TodoRepository todoRepository;
    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("할 일 추가 성공 테스트")
    void givenNewTodo_whenSaveTodo_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("test title")
                .content("test content")
                .build();

        // When
        ResponseEntity<TodoResponseDto> result = sut.createTodo(requestDto, user);

        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody().getUsername(), user.getUsername());
        assertEquals(result.getBody().getTitle(), requestDto.getTitle());
    }

    @Test
    @DisplayName("작성자가 아닌 다른 사용자가 수정 시도 시, 실패")
    void givenOtherUser_whenModifyTodo_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        User otherUser = createUser("noowner", "123456789");
        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("과제끝내")
                .content("오늘까지")
                .build();

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));
        ResponseEntity<?> result = sut.modifyTodo(1L, requestDto, otherUser);

        // Then
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(result.getBody(), "작성자만 수정 가능합니다.");
        assertNotEquals(todo.getTitle(), requestDto.getTitle());
    }

    @Test
    @DisplayName("본인이 작성한 할 일 수정 시도 시, 성공")
    void givenCorrectUser_whenModifyTodo_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo(user);

        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("과제끝내")
                .content("오늘까지")
                .build();

        // When
        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));
        ResponseEntity<?> result = sut.modifyTodo(1L, requestDto, user);
        TodoResponseDto body = (TodoResponseDto) result.getBody();

        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(body.getTitle(), requestDto.getTitle());
        assertEquals(body.getContent(), requestDto.getContent());
        assertEquals(body.getUsername(), user.getUsername());
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