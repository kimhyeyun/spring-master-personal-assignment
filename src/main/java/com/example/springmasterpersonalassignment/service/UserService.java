package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.dto.request.SignupRequest;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;

public interface UserService {

    /*
     * 회원 가입
     * @param SignupRequest : 회원 가입 요청 정보
     * @return UserResponse : 가입된 유저 응답 정보
     * */
    UserResponse signup(SignupRequest request);
}
