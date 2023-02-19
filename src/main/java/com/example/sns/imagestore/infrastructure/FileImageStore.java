package com.example.sns.imagestore.infrastructure;

import com.example.sns.imagestore.exception.ImageEmptyException;
import com.example.sns.imagestore.exception.ImageStoreException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileImageStore implements ImageStore {

    public static final int EXTENDER_INDEX = 1;

    private final String feedStorePath;
    private final String profileStorePath;

    public FileImageStore(@Value("${image.feed-store-path}") String feedStorePath,
                          @Value("${image.profile-store-path}") String profileStorePath) {
        this.feedStorePath = feedStorePath;
        this.profileStorePath = profileStorePath;
    }

    @Override
    public List<String> storeFeedImages(List<MultipartFile> feedImages) {
        validateEmptyImages(feedImages);

        return feedImages.stream()
                .map(feedImage -> storeImage(feedImage, feedStorePath))
                .collect(Collectors.toList());
    }

    @Override
    public String storeProfileImage(MultipartFile profileImage) {
        if (profileImage.isEmpty()) {
            throw new ImageEmptyException();
        }
        return storeImage(profileImage, profileStorePath);
    }

    private void validateEmptyImages(List<MultipartFile> feedImages) {
        if (feedImages.isEmpty()) {
            throw new ImageEmptyException();
        }

        feedImages.stream()
                .filter(MultipartFile::isEmpty)
                .findAny()
                .ifPresent(multipartFile -> {
                    throw new ImageEmptyException();
                });
    }

    private String storeImage(MultipartFile feedImage, String feedStorePath) {
        try {
            String imagePath = getImagePath(feedImage, feedStorePath);
            feedImage.transferTo(new File(imagePath));
            return imagePath;
        } catch (Exception e) {
            throw new ImageStoreException(e);
        }
    }

    private String getImagePath(MultipartFile feedImage, String storePath) {
        String ext = extractExt(feedImage);

        String newImageName = getNewImageName(ext);
        return storePath + newImageName;
    }

    private String extractExt(MultipartFile feedImage) {
        String originalFilename = feedImage.getOriginalFilename();
        return originalFilename.split("\\.")[EXTENDER_INDEX];
    }


    private String getNewImageName(String ext) {
        return UUID.randomUUID() + "." + ext;
    }
}
