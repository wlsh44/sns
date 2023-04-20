package com.example.sns.common.imagestore;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStore {
    String saveImage(MultipartFile profileImages, String storePath);
}
