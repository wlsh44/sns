package com.example.sns.common.imagestore.infrastructure;

import com.example.sns.common.imagestore.ImageStore;
import com.example.sns.common.imagestore.exception.InvalidImageException;
import com.example.sns.common.imagestore.exception.ImageStoreException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Profile({"local", "test"})
public class FileImageStore implements ImageStore {

    public static final int EXTENDER_INDEX = 1;

    @Override
    public String saveImage(MultipartFile profileImage, String storePath) {
        if (profileImage.isEmpty()) {
            throw new InvalidImageException();
        }
        return storeImage(profileImage, storePath);
    }

    private String storeImage(MultipartFile feedImage, String feedStorePath) {
        try {
            String imagePath = getImagePath(feedImage, feedStorePath);
            feedImage.transferTo(new File(imagePath));
            return imagePath;
        } catch (IOException e) {
            throw new ImageStoreException();
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
