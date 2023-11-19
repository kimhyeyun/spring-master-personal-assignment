package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.SignupRequestDto;
import com.example.springmasterpersonalassignment.dto.response.CommentResponseDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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


    // 확인을 위한 메서드 //
    public List<TodoResponseDto> findTodoList(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.get().getTodoList().stream().map(TodoResponseDto::of).toList();
    }

    public List<CommentResponseDto> findCommentList(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.get().getCommentList().stream().map(CommentResponseDto::of).toList();
    }
}
