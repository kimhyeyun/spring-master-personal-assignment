package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.dto.request.LoginRequest;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 실패 - 없는 사용자")
    void givenNonExistedUser_whenLogin_thenFail() throws Exception {
        // Given
        LoginRequest requestDto = new LoginRequest("whoareyou", "123456789");

        // When && Then
        MvcResult result = mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                )
                .andExpect(unauthenticated())
                .andDo(print())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        assertEquals(body, "401 : 회원을 찾을 수 없습니다.\n");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void givenWrongPassword_whenLogin_thenFail() throws Exception {
        // Given
        createUser("hyeyun", "123456789");

        LoginRequest requestDto = new LoginRequest("hyeyun", "456789123");

        // When && Then
        MvcResult result = mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                )
                .andExpect(unauthenticated())
                .andDo(print())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        assertEquals(body, "401 : 회원을 찾을 수 없습니다.\n");
    }

    @Test
    @DisplayName("로그인 성공")
    void givenLoginRequest_whenLogin_thenSuccess() throws Exception {
        // Given

        LoginRequest requestDto = new LoginRequest("hyeyun", "123456789");

        createUser(requestDto.username(), requestDto.password());

        // When
        // Then
        MvcResult result = mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertEquals(body, "200 : 로그인 성공\n");
    }

    private void createUser(String username, String password) {
        userRepository.saveAndFlush(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
    }
}
