package com.example.sns.common.infrastructure.imagestore;

import com.example.sns.common.infrastructure.imagestore.exception.ImageStoreException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStore {
    String saveImage(MultipartFile image, String storePath) throws ImageStoreException;
}
