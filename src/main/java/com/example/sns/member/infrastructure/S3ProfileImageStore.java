package com.example.sns.member.infrastructure;

import com.example.sns.common.infrastructure.imagestore.ImageStore;
import com.example.sns.common.infrastructure.imagestore.exception.InvalidImageException;
import com.example.sns.member.application.ProfileImageStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3ProfileImageStore implements ProfileImageStore {

    private final String storePath;
    private final ImageStore imageStore;

    public S3ProfileImageStore(@Value("${cloud.aws.s3.store-path.profile}") String storePath,
                               ImageStore imageStore) {
        this.storePath = storePath;
        this.imageStore = imageStore;
    }

    @Override
    public String saveProfileImage(MultipartFile image) {
        validateEmptyImages(image);

        return imageStore.saveImage(image, storePath);
    }

    private void validateEmptyImages(MultipartFile image) {
        if (image.isEmpty()) {
            throw new InvalidImageException();
        }
    }
}
