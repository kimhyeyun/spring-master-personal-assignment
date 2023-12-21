package com.example.springmasterpersonalassignment.repository;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.entity.Todo;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.springmasterpersonalassignment.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Todo> searchTodoBy(String type, String keyword, User user, Pageable pageable) {
        List<Todo> todos = jpaQueryFactory.selectFrom(todo)
                .where(searchKeyword(type, keyword))
                .orderBy(new OrderSpecifier<>(Order.DESC, todo.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(todos, pageable, todos.size());

    }

    private Predicate searchKeyword(String type, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }

        type = type.toLowerCase();
        return switch (type) {
            case "title" -> todo.title.contains(keyword);
            case "content" -> todo.content.contains(keyword);
            default -> throw new CustomException(ErrorCode.INVALID_TYPE);
        };

    }
}
