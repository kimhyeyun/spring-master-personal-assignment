package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.TodoRequest;
import com.example.springmasterpersonalassignment.dto.response.TodoResponse;
import com.example.springmasterpersonalassignment.entity.User;

import java.util.List;
import java.util.Map;

public interface TodoService {

    /*
    * 할 일 생성
    *
    * @param TodoRequest : 할 일 생성 요청 정보
    * @param User : 생성 요청 유저 정보
    * @return TodoResponse : 생성된 할 일 응답 정보
    * */
    TodoResponse createTodo(TodoRequest requestDto, User user);


    /*
    * 유저 별 할 일 조회
    *
    * @return Map<String, List<TodoResponse>> : 유저 별 할 일 응답 정보 맵
    * */
    Map<String, List<TodoResponse>> getTodoList();

    /*
    * 할 일 수정
    *
    * @param id : 수정할 할 일 id
    * @param TodoRequest : 할 일 수정 요청 정보
    * @param User : 수정 요청 유저 정보
    *
    * @return TodoResponse : 수정된 할 일 응답 정보
    * */
    TodoResponse modifyTodo(long id, TodoRequest requestDto, User user);

    /*
    * 할 일 삭제
    *
    * @param id : 삭제할 할 일 id
    * @param User : 삭제 요청한 유저 정보
    * */
    void deleteTodo(long id, User user);

    /*
    * 할 일 완료 처리
    *
    * @param id : 완료 처리할 할 일 id
    * @param User : 완료 처리 요청한 유저 정보
    *
    * @return : 완료 처리된 할 일 응답 정보
    * */
    TodoResponse finishedTodo(Long id, User user);

    /*
    * 할 일 검색
    *
    * @param type : 검색 타입(제목, 내용)
    * @param keyword : 검색어
    * @param cursor : 표시할 페이지 위치
    * @param size : 페이지 총 크기
    * @param User : 검색 요청한 유저 정보
    *
    * @return List<TodoResponse> : 검색한 할 일 응답 정보 리스트
    * */

    List<TodoResponse> searchTodo(String type, String keyword, Integer cursor, Integer size, User user);
}
