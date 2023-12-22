package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.MockSpringSecurityFilter;
import com.example.springmasterpersonalassignment.config.WebSecurityConfig;
import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.constant.SuccessCode;
import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import com.example.springmasterpersonalassignment.dto.response.CommentResponse;
import com.example.springmasterpersonalassignment.entity.Comment;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("[Comment Controller Test]")
class CommentControllerTest {

    private MockMvc mvc;
    private Principal principal;
    private UserDetailsImpl userDetails;

    @Autowired private ObjectMapper mapper;
    @Autowired private WebApplicationContext context;

    @MockBean private CommentServiceImpl commentService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();

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
    @DisplayName("댓글 저장 성공")
    void givenTodoIdAndCommentRequest_whenSave_thenSuccess() throws Exception {
        // Given
        Todo todo = createTodo(1L, "과제 완료", "9시 전까지", userDetails.getUser());
        CommentRequest commentRequestDto = new CommentRequest("화이팅");

        CommentResponse responseDto = new CommentResponse(1L, commentRequestDto.content(), userDetails.getUsername(), LocalDateTime.now(), LocalDateTime.now());

        given(commentService.createComment(anyLong(), any(), any(User.class))).willReturn(responseDto);

        // When & Then
        mvc.perform(post("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value(commentRequestDto.content()))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정 실패 - 권한 없음")
    void givenWrongUser_whenModify_thenFail() throws Exception {
        // Given
        Todo todo = createTodo(1L, "과제 완료", "9시 전까지", userDetails.getUser());
        User otherUser = createUser("whoareyou", "123456789");
        Comment comment = createComment("댓글 씀돠", todo, otherUser);

        CommentRequest commentRequestDto = new CommentRequest("화이팅");

        given(commentService.modifyComment(anyLong(), any(), any())).willThrow(new CustomException(ErrorCode.ACCESS_DENIED));

        // When & Then
        mvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal)
                )
                .andExpect(status().isForbidden())
                .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(CustomException.class)))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void givenCommentId_whenDelete_thenSuccess() throws Exception {
        // Given
        willDoNothing().given(commentService).deleteComment(anyLong(), any());

        // When & Then
        mvc.perform(delete("/api/comments/1")
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SuccessCode.SUCCESS_DELETE_COMMENT.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 권한 없음")
    void givenWrongUser_whenDelete_thenFail() throws Exception {
        // Given
        willThrow(new CustomException(ErrorCode.ACCESS_DENIED)).given(commentService).deleteComment(anyLong(), any());

        // When & Then
        mvc.perform(delete("/api/comments/1")
                        .principal(principal)
                )
                .andExpect(status().isForbidden())
                .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(CustomException.class)))
                .andDo(print());

    }

    private Comment createComment(String content, Todo todo, User user) {
        return Comment.builder()
                .content(content)
                .todo(todo)
                .user(user)
                .build();
    }

    private User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

    private Todo createTodo(long id, String title, String content, User user) {
        return Todo.builder()
                .id(id)
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
