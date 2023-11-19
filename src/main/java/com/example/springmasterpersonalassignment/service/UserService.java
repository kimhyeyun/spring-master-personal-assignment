package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.SignupRequestDto;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.jwt.JwtUtil;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j(topic = "userService")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 username 입니다.");
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("회원 가입 성공");
    }
}
