package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.TodoRequest;
import com.example.springmasterpersonalassignment.dto.response.TodoResponse;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public TodoResponse createTodo(TodoRequest requestDto, User user) {
        Todo todo = requestDto.toEntity(user);

        todoRepository.save(todo);

        return TodoResponse.of(todo);
    }

    @Override
    public Map<String,List<TodoResponse>> getTodoList() {
        Map<String, List<TodoResponse>> map = new TreeMap<>();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Todo> todos = todoRepository.findAllByUserOrderByCreatedAtDesc(user);
            map.put(user.getUsername(), todos.stream().map(TodoResponse::of).toList());
        }

        return map;
    }

    @Override
    @Transactional
    public TodoResponse modifyTodo(long id, TodoRequest requestDto, User user)  {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todo.modify(requestDto);
        return TodoResponse.of(todo);
    }

    @Override
    @Transactional
    public void deleteTodo(long id, User user) {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todoRepository.delete(todo);
    }

    @Override
    @Transactional
    public TodoResponse finishedTodo(Long id, User user) {
        Todo todo = findTodo(id);

        checkUser(user, todo);

        todo.setFinished(true);
        return TodoResponse.of(todo);
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
