package com.example.springmasterpersonalassignment.controller;

import com.example.springmasterpersonalassignment.MockSpringSecurityFilter;
import com.example.springmasterpersonalassignment.config.WebSecurityConfig;
import com.example.springmasterpersonalassignment.dto.request.LoginRequestDto;
import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import com.example.springmasterpersonalassignment.security.UserDetailsImpl;
import com.example.springmasterpersonalassignment.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = WebSecurityConfig.class
            )
        }
)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("[User Controller Test]")
class UserControllerTest {

    private MockMvc mvc;
    private Principal principal;

    @Autowired private ObjectMapper mapper;
    @Autowired private WebApplicationContext context;
    @MockBean private UserService userService;
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

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "", null);
    }

    @Test
    @DisplayName("회원 가입 요청 성공")
    void givenSignUpRequest_whenSignup_thenSuccess() throws Exception {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("kimhyeyun");
        signupRequestDto.setPassword("Password123456");

        // When
        // Then
        mvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signupRequestDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


}