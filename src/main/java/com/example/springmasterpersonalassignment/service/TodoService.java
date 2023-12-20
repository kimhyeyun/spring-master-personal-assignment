package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.TodoRequestDto;
import com.example.springmasterpersonalassignment.dto.response.TodoResponseDto;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
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
    public TodoResponseDto createTodo(TodoRequestDto requestDto, User user) {
        Todo todo = requestDto.toEntity(user);

        todoRepository.save(todo);

        return TodoResponseDto.of(todo);
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
    public TodoResponseDto modifyTodo(long id, TodoRequestDto requestDto, User user)  {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todo.modify(requestDto);
        return TodoResponseDto.of(todo);
    }

    @Transactional
    public void deleteTodo(long id, User user) {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todoRepository.delete(todo);
    }


    @Transactional
    public TodoResponseDto finishedTodo(Long id, User user) {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todo.setFinished(true);
        return TodoResponseDto.of(todo);
    }

    private Todo findTodo(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TODO)
        );
    }

    private static void checkUser(User user, Todo todo) {
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
}
