package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.CommentRequestDto;
import com.example.springmasterpersonalassignment.dto.response.CommentResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.CommentRepository;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks private CommentService sut;
    @Mock private CommentRepository commentRepository;
    @Mock private TodoRepository todoRepository;
    @Mock private UserRepository userRepository;

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 추가 시도 시, 실패")
    void givenNonExistedTodo_whenSave_thenFail() {
        // Given
        User user = createUser("hyeyun", "123456789");
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("퍼가요~^^");


        // When
        // Then
        assertThatThrownBy(() -> sut.createComment(1L, requestDto, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 id 입니다.");
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void givenCommentRequest_whenSave_thenSuccess() {
        // Given
        User user = createUser("hyeyun", "123456789");
        Todo todo = createTodo("TIL 작성", "12.01 9시 전 작성하자", user);
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("퍼가요~^^");

        given(todoRepository.findById(any())).willReturn(Optional.ofNullable(todo));

        // When
        ResponseEntity<CommentResponseDto> result = sut.createComment(1L, requestDto, user);
        CommentResponseDto body = result.getBody();

        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(body.getContent(), requestDto.getContent());
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