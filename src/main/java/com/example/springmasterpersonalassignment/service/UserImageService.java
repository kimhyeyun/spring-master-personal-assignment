package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserImageService {

    /*
    * 이미지 업로드
    *
    * @param User : 요청한 유저 정보
    * @param MultipartFile[] : 업로드할 이미지 파일들
    * */
    void uploadImages(User user, MultipartFile[] images);

    /*
     * 이미지 삭제
     *
     * @param User : 요청한 유저 정보
     * @param url : 삭제할 이미지 url들
     * */
    void deleteImage(User user, String url);

    /*
     * 이미지 전체 삭제
     *
     * @param username : 요청한 유저 아이디
     * */
    void deleteAll(String username);
}
