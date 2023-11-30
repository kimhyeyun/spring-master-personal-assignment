package com.example.springmasterpersonalassignment.entity;

import com.example.springmasterpersonalassignment.IntegrationTest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class EntityValidateTest extends IntegrationTest {

    @Autowired private Validator validator;

    @Test
    @DisplayName("회원 가입시, 잘못된 길이의 아이디, 비밀번호 입력 시 실패")
    void givenWrongLengthOfUsernameOrPassword_whenSignup_thenFail() {
        // given
        String username = "te";
        String password = "123";

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);

        // when
        Set<ConstraintViolation<SignupRequestDto>> validate = validator.validate(requestDto);

        // then
        Iterator<ConstraintViolation<SignupRequestDto>> iterator = validate.iterator();
        List<String> message = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<SignupRequestDto> next = iterator.next();
            message.add(next.getMessage());
        }

        assertThat(message).contains("길이가 4에서 10 사이여야 합니다");
        assertThat(message).contains("길이가 8에서 15 사이여야 합니다");
    }

    @Test
    @DisplayName("회원 가입시, 잘못된 형식의 아이디, 비밀번호 입력 시 실패")
    void givenWrongPatternOfUsernameOrPassword_whenSignup_thenFail() {
        // Given
        String username = "테스트아이디Z";
        String password = "테스트비밀번호123456";

        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);

        // when
        Set<ConstraintViolation<SignupRequestDto>> validate = validator.validate(requestDto);

        // then
        Iterator<ConstraintViolation<SignupRequestDto>> iterator = validate.iterator();
        List<String> message = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<SignupRequestDto> next = iterator.next();
            message.add(next.getMessage());
        }

        assertThat(message).contains("영어 소문자와 숫자로만 입력해주세요.");
        assertThat(message).contains("영어와 숫자로만 입력해주세요.");
    }
}
