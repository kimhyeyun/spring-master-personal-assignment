package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.IntegrationTest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


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
    @DisplayName("올바른 아이디, 비밀번호 입력 시, 회원 가입 성공")
    void givenUsernameAndPassword_whenSignup_thenSuccess() {

    }

    private User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

}