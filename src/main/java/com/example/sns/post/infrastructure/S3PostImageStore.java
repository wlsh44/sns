package com.example.sns.post.infrastructure;

import com.example.sns.common.imagestore.ImageStore;
import com.example.sns.common.imagestore.exception.InvalidImageException;
import com.example.sns.post.application.PostImageStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile({"dev", "prod"})
public class S3PostImageStore implements PostImageStore {

    private final String storePath;
    private final ImageStore s3ImageStore;

    public S3PostImageStore(@Value("${ncp.s3.stor-path.post}") String storePath,
                            ImageStore s3ImageStore) {
        this.storePath = storePath;
        this.s3ImageStore = s3ImageStore;
    }

    @Override
    public List<String> savePostImages(List<MultipartFile> images) {
        validateEmptyImages(images);

        return images.stream()
                .map(feedImage -> s3ImageStore.saveImage(feedImage, storePath))
                .collect(Collectors.toList());
    }

    private void validateEmptyImages(List<MultipartFile> feedImages) {
        if (feedImages.isEmpty()) {
            throw new InvalidImageException();
        }

        feedImages.stream()
                .filter(MultipartFile::isEmpty)
                .findAny()
                .ifPresent(multipartFile -> {
                    throw new InvalidImageException();
                });
    }
}
