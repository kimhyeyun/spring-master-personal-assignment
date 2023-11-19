package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j(topic = "TodoService")
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public ResponseEntity<TodoResponseDto> createTodo(TodoRequestDto requestDto, User user) {
        Todo todo = requestDto.toEntity();
        todo.setUser(user);

        todoRepository.save(todo);

        return ResponseEntity.status(HttpStatus.OK).body(TodoResponseDto.of(todo));
    }

    public List<TodoResponseDto> getTodoList() {
        List<Todo> todos = todoRepository.findAllByOrderByCreatedAtDesc();

        return todos.stream().map(TodoResponseDto::of).toList();
    }
}
