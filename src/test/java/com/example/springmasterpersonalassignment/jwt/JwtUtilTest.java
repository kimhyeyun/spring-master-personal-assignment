package com.example.springmasterpersonalassignment.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.springmasterpersonalassignment.jwt.JwtUtil.BEARER_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest implements CommonTest {

    @Autowired JwtUtil jwtUtil;
    @Mock private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        jwtUtil.init();
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        //when
        String token = jwtUtil.createToken(TEST_USER_NAME);

        //then
        assertNotNull(token);
    }

    @Test
    @DisplayName("토큰 추출")
    void resolveToken() {
        // Given
        var token = "test-token";
        var bearerToken = BEARER_PREFIX + token;

        // When
        given(request.getHeader(JwtUtil.AUTHORIZATION_HEADER)).willReturn(bearerToken);
        var resolvedToken = jwtUtil.getJwtFromHeader(request);

        // Then
        assertEquals(resolvedToken, token);
    }


    @DisplayName("토큰 검증")
    @Nested
    class validateToken {

        @Test
        @DisplayName("토큰 검증 성공")
        void validateToken_success() {
            // Given
            String token = jwtUtil.createToken(TEST_USER_NAME).substring(7);

            // When
            boolean isValid = jwtUtil.validateToken(token);

            // Then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("토큰 검증 실패 - 유효하지 않은 토큰")
        void validateToken_fail() {
            // Given
            String invalidToken = "invalid-token";

            // When
            boolean isValid = jwtUtil.validateToken(invalidToken);

            // Then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("토큰에서 UserInfo 추출")
        void getUserInfoFromToken() {
            // Given
            String token = jwtUtil.createToken(TEST_USER_NAME).substring(7);

            // When
            Claims claims = jwtUtil.getUserInfoFromToken(token);

            // Then
            assertNotNull(claims);
            assertEquals(TEST_USER_NAME, claims.getSubject());
        }
    }
}
