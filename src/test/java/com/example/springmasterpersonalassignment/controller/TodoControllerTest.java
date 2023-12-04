package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.MockSpringSecurityFilter;
import com.example.springmasterpersonalassignment.config.WebSecurityConfig;
import com.example.springmasterpersonalassignment.dto.request.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.TodoService;
import com.example.springmasterpersonalassignment.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {TodoController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("[Todo Controller Test]")
class TodoControllerTest {

    private MockMvc mvc;
    private Principal principal;
    private UserDetailsImpl userDetails;

    @Autowired private ObjectMapper mapper;
    @Autowired private WebApplicationContext context;

    @MockBean private TodoService todoService;
    @MockBean private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        String username = "kimhyeyun";
        String password = "Password123456";

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

         userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    
    @Test
    @DisplayName("할 일 저장 성공")
    void givenTodoRequest_whenSave_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("뭐하지")
                .content("공부하자")
                .build();

        Todo todo = createTodo(requestDto.getTitle(), requestDto.getContent(), userDetails.getUser());

        // When
        TodoResponseDto response = TodoResponseDto.of(todo);
        given(todoService.createTodo(any(), any())).willReturn(response);
        
        // Then
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.content").value(requestDto.getContent()))
                .andDo(print());

    }

    @Test
    @DisplayName("작성자와 수정자가 다른 경우 수정 실패")
    void givenOtherUser_whenModify_thenFail() throws Exception {
        // Given
        this.mockUserSetup();
        User otherUser = createUser("whoareyou", "123456789");

        Todo todo = createTodo("뭐하지", "공부하자", otherUser);
        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("변경하자")
                .content("휴식이다")
                .build();

        CustomException c = new CustomException("권한이 없습니다.");
        given(todoService.modifyTodo(anyLong(), any(), any())).willThrow(c);

        // When
        // Then
        mvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(CustomException.class)))
                .andDo(print());
    }


    @Test
    @DisplayName("수정 성공")
    void givenRequest_whenModify_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        Todo todo = createTodo("뭐하지", "공부하자", userDetails.getUser());
        TodoRequestDto requestDto = TodoRequestDto.builder()
                .title("변경하자")
                .content("휴식이다")
                .build();

        todo.modify(requestDto);

        TodoResponseDto response = TodoResponseDto.of(todo);
        given(todoService.modifyTodo(anyLong(), any(), any())).willReturn(response);

        // When
        // Then
        mvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(requestDto.getTitle()))
                .andExpect(jsonPath("$.content").value(requestDto.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 삭제 요청 시, 실패")
    void givenOtherUser_whenDelete_thenFail() throws Exception {
        // Given
        this.mockUserSetup();

        CustomException c = new CustomException("권한이 없습니다.");
        given(todoService.deleteTodo(anyLong(), any())).willThrow(c);

        // When
        // Then
        mvc.perform(delete("/api/todos/1")
                        .principal(principal)
                )
                .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(CustomException.class)))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 삭제 성공")
    void givenTodoId_whenDelete_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        // When & Then
        Todo todo = createTodo("뭐하지", "과제 제출", userDetails.getUser());

        String result = "삭제 성공";
        given(todoService.deleteTodo(anyLong(), any())).willReturn(result);

        mvc.perform(delete("/api/todos/1")
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("삭제 성공"))
                .andDo(print());
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