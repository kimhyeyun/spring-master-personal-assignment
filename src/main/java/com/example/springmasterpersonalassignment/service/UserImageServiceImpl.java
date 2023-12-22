package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.entity.UserImage;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserImageServiceImpl implements UserImageService{

    private final UserImageRepository userImageRepository;
    private final S3Service s3Service;

    public final static String USER_PATH_PREFIX = "users/";

    @Override
    public void uploadImages(User user, MultipartFile[] images) {
        for (MultipartFile image : images) {
            try {
                String imageUrl = s3Service.saveImages(
                        USER_PATH_PREFIX + user.getUsername() + "/",
                        image
                );

                UserImage userImage = new UserImage(user, imageUrl);
                userImageRepository.save(userImage);

            } catch (IOException e) {
                throw new CustomException(ErrorCode.CAN_NOT_READ_IMAGE);
            }
        }
    }

    @Override
    public void deleteImage(User user, String url) {
        s3Service.deleteFile(url);
        for (UserImage userImage : user.getImages()) {
            user.getImages().remove(userImage);
            userImageRepository.delete(userImage);
            break;
        }
    }

    @Override
    public void deleteAll(String username) {
        s3Service.deleteAllUserFiles(username);
        List<UserImage> userImages = userImageRepository.findAllByUser_Username(username);
        userImageRepository.deleteAll(userImages);
    }
}
