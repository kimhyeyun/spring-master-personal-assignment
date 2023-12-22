package com.example.springmasterpersonalassignment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.springmasterpersonalassignment.constant.ErrorCode;
import com.example.springmasterpersonalassignment.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.springmasterpersonalassignment.service.UserImageServiceImpl.USER_PATH_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public String saveImages(String prefix, MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String fileNameExtension = StringUtils.getFilenameExtension(fileName);

        fileName = prefix + UUID.randomUUID() + "." + fileNameExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

        String accessUrl = amazonS3Client.getUrl(bucket, fileName).toString();

        return accessUrl;
    }

    @Transactional
    public void deleteFile(String fileUrl) {
        amazonS3Client.deleteObject(bucket, extractKey(fileUrl));
    }
    @Transactional
    public void deleteAllUserFiles(String username) {
        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(USER_PATH_PREFIX + username + "/");

        List<S3ObjectSummary> objectSummaries = amazonS3Client.listObjects(request).getObjectSummaries();

        for (S3ObjectSummary summary : objectSummaries) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, summary.getKey()));
        }
    }

    private String extractKey(String fileUrl) {
        String regex = "(users/.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new CustomException(ErrorCode.INVALID_POST_FILE_URL);
    }

}
