package com.example.springmasterpersonalassignment.service;

import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.dto.request.DeleteImageRequest;
import com.example.springmasterpersonalassignment.dto.request.SignupRequest;
import com.example.springmasterpersonalassignment.dto.response.UserResponse;
import com.example.springmasterpersonalassignment.entity.User;
import com.example.springmasterpersonalassignment.exception.CustomException;
import com.example.springmasterpersonalassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageService userImageService;

    @Override
    @Transactional
    public UserResponse signup(SignupRequest request) {
        String username = request.username();

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXISTED_USERNAME);
        }

        User user = userRepository.save(request.toEntity(passwordEncoder));

        return UserResponse.of(user);
    }

    @Override
    @Transactional
    public UserResponse uploadImage(String username, MultipartFile[] images, User user) {
        if (!user.getUsername().equals(username)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        User findUser = findUser(username);
        try {
            userImageService.uploadImages(findUser, images);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CAN_NOT_READ_IMAGE);
        }

        return UserResponse.of(findUser);
    }

    @Override
    @Transactional
    public UserResponse deleteImage(String username, DeleteImageRequest request, User user) {
        if (!user.getUsername().equals(username)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        User findUser = findUser(username);

        try {
            for (String url : request.deleteFileUrl()) {
                userImageService.deleteImage(findUser, url);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return UserResponse.of(findUser);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
