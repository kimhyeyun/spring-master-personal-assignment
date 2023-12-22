package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.DeleteImageRequest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequest;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;
import com.example.springmasterpersonalassignment.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /*
     * 회원 가입
     * @param SignupRequest : 회원 가입 요청 정보
     * @return UserResponse : 가입된 유저 응답 정보
     * */
    UserResponse signup(SignupRequest request);

    /*
    * 이미지 업로드
    *
    * @param username : 업로드할 유저 아이디
    * @param images : 업로드 할 이미지 파일들
    * @param user : 업로드 요청한 유저 정보
    *
    * @return UserResponse : 유저 응답 정보
    * */
    UserResponse uploadImage(String username, MultipartFile[] images, User user);

    /*
     * 이미지 삭제
     *
     * @param username : 이미지 삭제할 유저 아이디
     * @param DeleteImageRequest : 삭제 할 이미지 요청 정보
     * @param user : 이미지 삭제 요청한 유저 정보
     *
     * @return UserResponse : 유저 응답 정보
     * */
    UserResponse deleteImage(String username, DeleteImageRequest request, User user);

}
