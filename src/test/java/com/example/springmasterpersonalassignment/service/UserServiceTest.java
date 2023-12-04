package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("[UserService] 테스트")
class UserServiceTest{

    @InjectMocks UserService userService;
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("중복된 아이디 입력 시, 회원 가입 실패")
    void givenIsExistedUsername_whenSignup_thenFail() {
        // Given
        String username = "tester";
        String password = "123456789";

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);

        User user = createUser(username, password);

        // When
        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.ofNullable(user));
        ResponseEntity<?> answer = userService.signup(requestDto);

        // Then
        assertEquals(answer.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(answer.getBody().equals("중복된 username 입니다."), true);
    }

    @Test
    @DisplayName("올바른 아이디, 비밀번호 입력 시, 회원 가입 성공")
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
        assertEquals(result.getBody().equals("회원 가입 성공"), true);
    }


    private User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}