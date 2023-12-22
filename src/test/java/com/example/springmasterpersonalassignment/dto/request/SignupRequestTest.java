package com.example.springmasterpersonalassignment.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 가입 요청 유효성 검증")
class SignupRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @DisplayName("username 검증")
    @Nested
    class UsernameTest {

        @Test
        @DisplayName("4자리 일 때, 성공")
        void given4LenUsername_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("test", "123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("10자리 일 때, 성공")
        void given10LenUsername_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("testertest", "123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("소문자와 숫자로 입력될 때, 성공")
        void givenLowerAlphabetAndNumberUsername_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("test123", "123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("입력이 없을 때, 실패")
        void givenNullUsername_whenRequest_thenFail() {
            // Given
            SignupRequest request = new SignupRequest("", "123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).hasSize(3);
        }

        @Test
        @DisplayName("특수 문자와 대문자 입력 시, 실패")
        void givenSpecialAndUpperAlphabet_whenRequest_thenFail() {
            // Given
            SignupRequest request = new SignupRequest("!Abcd12", "123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).hasSize(1);
        }

    }

    @DisplayName("password 검증")
    @Nested
    class PasswordTest {

        @Test
        @DisplayName("8자리 일 때, 성공")
        void given8LenPassword_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("test", "12345678");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("15자리 일 때, 성공")
        void given10LenUsername_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("test", "012345678912345");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("영어와 숫자로 입력될 때, 성공")
        void givenAlphabetAndNumberUsername_whenRequest_thenSuccess() {
            // Given
            SignupRequest request = new SignupRequest("test", "abAB123456789");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).isEmpty();
        }

        @Test
        @DisplayName("입력이 없을 때, 실패")
        void givenNullPasswird_whenRequest_thenFail() {
            // Given
            SignupRequest request = new SignupRequest("test", "");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).hasSize(3);
        }

        @Test
        @DisplayName("특수 문자와 한글 입력 시, 실패")
        void givenSpecialAndUpperAlphabet_whenRequest_thenFail() {
            // Given
            SignupRequest request = new SignupRequest("test", "!@#ㄴㅇㄹㄴㅇㄹ");
            // When
            Set<ConstraintViolation<SignupRequest>> validations = validator.validate(request);
            // Then
            assertThat(validations).hasSize(1);
        }

    }


}