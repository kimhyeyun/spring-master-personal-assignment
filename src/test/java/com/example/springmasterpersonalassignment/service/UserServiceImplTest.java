package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.SignupRequest;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("[UserService] 테스트")
class UserServiceImplTest {

    @InjectMocks UserServiceImpl sut;
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("중복된 아이디 입력 시, 회원 가입 실패")
    void givenIsExistedUsername_whenSignup_thenFail() {
        // Given
        String username = "tester";
        String password = "123456789";

        SignupRequest requestDto = new SignupRequest(username, password);
        User user = createUser(username, password);

        // When
        when(userRepository.findByUsername(requestDto.username())).thenReturn(Optional.ofNullable(user));

        // Then
        assertThatThrownBy(() -> sut.signup(requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ALREADY_EXISTED_USERNAME.getMessage());

    }

    @Test
    @DisplayName("올바른 아이디, 비밀번호 입력 시, 회원 가입 성공")
    void givenUsernameAndPassword_whenSignup_thenSuccess() {
        // Given
        SignupRequest requestDto = new SignupRequest("tester", "123456789");
        User user = requestDto.toEntity(passwordEncoder);

        // When
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponse response = sut.signup(requestDto);

        // Then
        then(userRepository).should().save(any(User.class));
        assertEquals(response.username(), requestDto.username());

    }


    private User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
