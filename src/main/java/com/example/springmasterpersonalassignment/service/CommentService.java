package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import com.example.springmasterpersonalassignment.dto.response.CommentResponse;
import com.example.springmasterpersonalassignment.entity.Comment;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.CommentRepository;
import com.example.springmasterpersonalassignment.repository.TodoRepository;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final UserRepository userRepository;

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(Long todoId, CommentRequest requestDto, User user) {
        Todo todo = findTodo(todoId);

        Comment comment = requestDto.toEntity();
        comment.setUser(user);
        comment.setTodo(todo);

        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public List<CommentResponse> getCommentListByTodoId(Long todoId, User user) {
        Todo todo = findTodo(todoId);

        return todo.getComments().stream().map(CommentResponse::of).toList();
    }


    @Transactional
    public CommentResponse modifyComment(Long commentId, CommentRequest requestDto, User user) {
        Comment comment = findComment(commentId);

        checkUser(user, comment);

        comment.modify(requestDto);

        return CommentResponse.of(comment);
    }

    public Map<String, List<CommentResponse>> getCommentListByUser() {
        Map<String, List<CommentResponse>> map = new TreeMap<>();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Comment> comments = commentRepository.findAllByUser(user);
            map.put(user.getUsername(), comments.stream().map(CommentResponse::of).toList());
        }

        return map;
    }


    @Transactional
    public String  deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);

        checkUser(user, comment);

        commentRepository.delete(comment);
        return "삭제 성공";
    }

    private Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TODO)
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
        );
    }

    private static void checkUser(User user, Comment comment) {
        if (!comment.getUser().getUsername().equals(user.getUsername())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

    }
}
