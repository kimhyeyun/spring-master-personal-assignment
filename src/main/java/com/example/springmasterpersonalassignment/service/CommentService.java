package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.CommentRequest;
import com.example.springmasterpersonalassignment.dto.response.CommentResponse;
import com.example.springmasterpersonalassignment.entity.User;

import java.util.List;
import java.util.Map;

public interface CommentService {

    /*
    * 댓글 생성
    *
    * @param todoId : 댓글을 달 할 일 id
    * @param CommentRequest : 댓글 생성 요청 정보
    * @param User : 댓글 생성 유저 정보
    *
    * @return CommentResponse : 생성된 댓글 응답 정보
    * */
    CommentResponse createComment(Long todoId, CommentRequest requestDto, User user);

    /*
    * 할 일 별 댓글 리스트 조회
    *
    * @param todoId : 조회할 할 일 id
    * @param User : 조회를 요청한 유저 정보
    *
    * @return List<CommentResponse> : 댓글 응답 정보 리스트
    * */
    List<CommentResponse> getCommentListByTodoId(Long todoId, User user);


    /*
    * 댓글 수정
    *
    * @param commentId : 수정할 댓글 id
    * @param CommentRequest : 댓글 수정 요청 정보
    * @param User : 댓글 수정 요청 유저 정보
    *
    * @return CommentResponse : 수정된 댓글 응답 정보
    * */
    CommentResponse modifyComment(Long commentId, CommentRequest requestDto, User user);

    /*
    * 유저별 댓글 리스트 조회
    *
    * @return Map<String,List<CommentResponse>> : 유저별 댓글 응답 정보 리스트 맵
    * */
    Map<String, List<CommentResponse>> getCommentListByUser();

    /*
    * 댓글 삭제
    *
    * @param commentId : 삭제할 댓글 id
    * @param User : 삭제 요청한 유저 정보
    * */
    void deleteComment(Long commentId, User user);
}
