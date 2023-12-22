package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.MockSpringSecurityFilter;
import com.example.springmasterpersonalassignment.config.WebSecurityConfig;
import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.TodoRequest;
import com.example.springmasterpersonalassignment.dto.response.TodoResponse;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
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

        TodoRequest requestDto = new TodoRequest("뭐하지", "공부하자");

        Todo todo = createTodo(requestDto.title(), requestDto.content(), userDetails.getUser());

        // When
        TodoResponse response = TodoResponse.of(todo);
        given(todoService.createTodo(any(), any())).willReturn(response);
        
        // Then
        mvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(requestDto.title()))
                .andExpect(jsonPath("$.data.content").value(requestDto.content()))
                .andExpect(jsonPath("$.data.username").value(userDetails.getUser().getUsername()))
                .andDo(print());
    }

    @Test
    @DisplayName("작성자와 수정자가 다른 경우 수정 실패")
    void givenOtherUser_whenModify_thenFail() throws Exception {
        // Given
        this.mockUserSetup();
        User otherUser = createUser("whoareyou", "123456789");

        Todo todo = createTodo("뭐하지", "공부하자", otherUser);
        TodoRequest requestDto = new TodoRequest("변경하자", "휴식이다");

        CustomException c = new CustomException(ErrorCode.ACCESS_DENIED);
        given(todoService.modifyTodo(anyLong(), any(), any())).willThrow(c);

        // When && Then
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
        TodoRequest requestDto = new TodoRequest("변경하자", "휴식이다");

        todo.modify(requestDto);

        TodoResponse response = TodoResponse.of(todo);
        given(todoService.modifyTodo(anyLong(), any(), any())).willReturn(response);

        // When && Then
        mvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(requestDto.title()))
                .andExpect(jsonPath("$.data.content").value(requestDto.content()))
                .andDo(print());
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 삭제 요청 시, 실패")
    void givenOtherUser_whenDelete_thenFail() throws Exception {
        // Given
        this.mockUserSetup();

        willThrow(new CustomException(ErrorCode.ACCESS_DENIED)).given(todoService).deleteTodo(anyLong(), any());

        // When && Then
        mvc.perform(delete("/api/todos/1")
                        .principal(principal)
                )
                .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(CustomException.class)))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCESS_DENIED.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("할 일 삭제 성공")
    void givenTodoId_whenDelete_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        // When & Then
        Todo todo = createTodo("뭐하지", "과제 제출", userDetails.getUser());
        willDoNothing().given(todoService).deleteTodo(anyLong(), any());

        mvc.perform(delete("/api/todos/1")
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessCode.SUCCESS_DELETE_TODO.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 완료 처리 실패 - 권한 없음")
    void givenWrongUser_whenIsFinished_thenFail() throws Exception {
        // Given
        this.mockUserSetup();
        given(todoService.finishedTodo(eq(1L), any(User.class))).willThrow(new CustomException(ErrorCode.ACCESS_DENIED));

        // When & Then
        mvc.perform(put("/api/todos/finish/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 완료 처리 성공")
    void givenTodoId_whenIsFinished_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        TodoResponse responseDto = new TodoResponse(1L, "과제 완료", "하자!", userDetails.getUsername(), true, LocalDateTime.now(), LocalDateTime.now());

        given(todoService.finishedTodo(eq(1L), any(User.class))).willReturn(responseDto);

        // When & Then
        mvc.perform(put("/api/todos/finish/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(responseDto.title()))
                .andExpect(jsonPath("$.data.content").value(responseDto.content()))
                .andExpect(jsonPath("$.data.isFinished").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 검색 성공")
    void givenSearchRequest_whenSearch_thenSuccess() throws Exception {
        // Given
        this.mockUserSetup();

        List<TodoResponse> responses = List.of(
                new TodoResponse(1L, "title1", "content1", userDetails.getUsername(), true, LocalDateTime.now(), LocalDateTime.now()),
                new TodoResponse(2L, "title2", "content2", userDetails.getUsername(), false, LocalDateTime.now(), LocalDateTime.now()),
                new TodoResponse(3L, "title3", "content3", userDetails.getUsername(), false, LocalDateTime.now(), LocalDateTime.now()),
                new TodoResponse(4L, "title4", "content4", userDetails.getUsername(), true, LocalDateTime.now(), LocalDateTime.now())
        );

        given(todoService.searchTodo(any(), any(), any(), any(), any())).willReturn(responses);

        // When && Then
        mvc.perform(get("/api/todos")
                        .param("keyword", "title")
                        .param("type", "title")
                        .param("cursor", "0")
                        .param("size", "4")
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessCode.SUCCESS_SEARCH_TODO.getMessage()))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andDo(print());
    }

    @Test
    @DisplayName("할 일 검색 실패")
    void givenWrongSearchRequest_whenSearch_thenFail() throws Exception {
        // Given
        this.mockUserSetup();

        given(todoService.searchTodo(any(), any(), any(), any(), any())).willThrow(new CustomException(ErrorCode.INVALID_TYPE));

        // When && Then
        mvc.perform(get("/api/todos")
                        .param("keyword", "cont")
                        .param("type", "title")
                        .param("cursor", "0")
                        .param("size", "4")
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TYPE.getMessage()))
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
