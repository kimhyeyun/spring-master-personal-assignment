package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.IntegrationTest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@DisplayName("[UserService]")
class UserServiceTest extends IntegrationTest {

    @Autowired UserService userService;

    @Test
    @DisplayName("중복된 아이디 입력 시, 회원 가입 실패")
    void givenIsExistedUsername_whenSignup_thenFail() {
        // Given
        String username = "tester";
        String password = "123456789";

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);

        // When
        userService.signup(requestDto);

        // Then
        assertEquals(userService.signup(requestDto).getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("올바른 아이디, 비밀번호 입력 시, 회원 가입 성")
    void givenUsernameAndPassword_whenSignup_thenSuccess() {
        // Given
        String username = "tester";
        String password = "123456789";

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);

        // When
        ResponseEntity<?> result = userService.signup(requestDto);

        // Then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

}