package com.example.sns.post.application;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostImageStore {
    List<String> savePostImages(List<MultipartFile> images);
}
