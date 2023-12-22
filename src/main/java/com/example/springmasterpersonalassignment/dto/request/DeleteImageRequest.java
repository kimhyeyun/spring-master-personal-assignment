package com.example.springmasterpersonalassignment.dto.request;

import java.util.List;

public record DeleteImageRequest (
        List<String> deleteFileUrl
) {

}
