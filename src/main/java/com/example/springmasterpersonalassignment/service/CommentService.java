package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.CommentRequestDto;
import com.example.springmasterpersonalassignment.dto.response.CommentResponseDto;
import com.example.springmasterpersonalassignment.entity.Comment;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.CommentRepository;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j(topic = "CommentService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final UserRepository userRepository;

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(Long todoId, CommentRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 id 입니다.")
        );

        Comment comment = requestDto.toEntity();
        comment.setUser(user);
        comment.setTodo(todo);

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(CommentResponseDto.of(comment));
    }

    public List<CommentResponseDto> getCommentListByTodoId(Long todoId, User user) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 id 입니다.")
        );

        return todo.getComments().stream().map(CommentResponseDto::of).toList();
    }


    @Transactional
    public ResponseEntity<?> modifyComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 id 입니다.")
        );

        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 수정 가능합니다.");
        }

        comment.modify(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(CommentResponseDto.of(comment));
    }

    public Map<String, List<CommentResponseDto>> getCommentListByUser() {
        Map<String, List<CommentResponseDto>> map = new TreeMap<>();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Comment> comments = commentRepository.findAllByUser(user);
            map.put(user.getUsername(), comments.stream().map(CommentResponseDto::of).toList());
        }

        return map;
    }
}
