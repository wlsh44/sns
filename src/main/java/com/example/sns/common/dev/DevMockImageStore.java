package com.example.sns.common.dev;

import com.example.sns.common.infrastructure.imagestore.FileNameGenerator;
import com.example.sns.common.infrastructure.imagestore.ImageStore;
import com.example.sns.common.infrastructure.imagestore.exception.ImageStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Profile({"local", "dev", "test"})
@Component
@RequiredArgsConstructor
public class DevMockImageStore implements ImageStore {

    private final FileNameGenerator fileNameGenerator;

    @Override
    public String saveImage(MultipartFile image, String storePath) throws ImageStoreException {
        return fileNameGenerator.generateFileName(image, storePath);
    }
}
