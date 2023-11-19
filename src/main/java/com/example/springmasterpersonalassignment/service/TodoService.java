package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
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

@Slf4j(topic = "TodoService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<TodoResponseDto> createTodo(TodoRequestDto requestDto, User user) {
        Todo todo = requestDto.toEntity();
        todo.setUser(user);

        todoRepository.save(todo);

        return ResponseEntity.status(HttpStatus.OK).body(TodoResponseDto.of(todo));
    }

    public Map<String,List<TodoResponseDto>> getTodoList() {
        Map<String, List<TodoResponseDto>> map = new TreeMap<>();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Todo> todos = todoRepository.findAllByUserOrderByCreatedAtDesc(user);
            map.put(user.getUsername(), todos.stream().map(TodoResponseDto::of).toList());
        }

        return map;
    }

    @Transactional
    public ResponseEntity<?> modifyTodo(long id, TodoRequestDto requestDto, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 id 입니다.")
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 수정 가능합니다.");
        }

        todo.modify(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(TodoResponseDto.of(todo));
    }

    @Transactional
    public ResponseEntity<?> deleteTodo(long id, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존자하지 않는 id 입니다.")
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 수정 가능합니다.");
        }

        todoRepository.delete(todo);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }


    @Transactional
    public ResponseEntity<?> finishedTodo(Long id, User user) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존자하지 않는 id 입니다.")
        );

        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 수정 가능합니다.");
        }

        todo.setFinished(true);
        return ResponseEntity.status(HttpStatus.OK).body("완료 처리 성공");
    }
}
