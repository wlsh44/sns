package com.example.sns.common.infrastructure.imagestore;

import com.example.sns.common.infrastructure.imagestore.exception.InvalidImageException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class FileNameGenerator {

    public static final String IMAGE_CONTENT_TYPE_DIVIDER = "/";

    public String generateFileName(MultipartFile multipartFile, String storePath) {
        String ext = extractExt(multipartFile);
        String newImageName = getNewImageName(ext);
        return storePath + newImageName;
    }

    private String extractExt(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (!StringUtils.hasText(contentType)) {
            throw new InvalidImageException();
        }
        return contentType.substring(contentType.lastIndexOf(IMAGE_CONTENT_TYPE_DIVIDER) + 1);
    }

    private String getNewImageName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }
}
