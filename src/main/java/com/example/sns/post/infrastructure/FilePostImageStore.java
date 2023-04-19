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
@Profile({"local", "test"})
public class FilePostImageStore implements PostImageStore {

    private final String storePath;
    private final ImageStore fileImageStore;

    public FilePostImageStore(@Value("${image.post-store-path}") String storePath,
                              ImageStore fileImageStore) {
        this.storePath = storePath;
        this.fileImageStore = fileImageStore;
    }

    @Override
    public List<String> savePostImages(List<MultipartFile> images) {
        validateEmptyImages(images);

        return images.stream()
                .map(feedImage -> fileImageStore.saveImage(feedImage, storePath))
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
