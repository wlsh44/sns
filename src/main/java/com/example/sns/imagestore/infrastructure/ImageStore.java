package com.example.sns.imagestore.infrastructure;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStore {
    List<String> storeFeedImages(List<MultipartFile> feedImages);
    String storeProfileImage(MultipartFile profileImages);
}
