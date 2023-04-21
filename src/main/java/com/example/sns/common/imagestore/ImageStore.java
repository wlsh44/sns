package com.example.sns.common.imagestore;

import com.example.sns.common.imagestore.exception.ImageStoreException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStore {
    String saveImage(MultipartFile image, String storePath) throws ImageStoreException;
}
