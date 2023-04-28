package com.example.sns.common.imagestore.infrastructure;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.sns.common.imagestore.ImageStore;
import com.example.sns.common.imagestore.exception.ImageStoreException;
import com.example.sns.common.imagestore.exception.TemporaryFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.example.sns.common.imagestore.exception.TemporaryFileException.CREATE_ERROR;
import static com.example.sns.common.imagestore.exception.TemporaryFileException.TRANSFER_ERROR;

@Slf4j
@Component
public class S3ImageStore implements ImageStore {

    private final String bucket;
    private final String tempFilePath;
    private final AmazonS3 s3Client;
    private final FileNameGenerator fileNameGenerator;

    public S3ImageStore(@Value("${cloud.aws.s3.bucket}") String bucket,
                        @Value("${cloud.aws.s3.store-path.temp}") String tempFilePath,
                        AmazonS3 s3Client,
                        FileNameGenerator fileNameGenerator) {
        this.bucket = bucket;
        this.tempFilePath = tempFilePath;
        this.s3Client = s3Client;
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public String saveImage(MultipartFile multipartFile, String storePath) {
        return upload(multipartFile, storePath);
    }

    private String upload(MultipartFile multipartFile, String storePath) {
        String fileName = fileNameGenerator.generateFileName(multipartFile, storePath);
        File file = convertToFile(multipartFile);

        try {
            return putImageToS3(file, fileName);
        } catch (SdkClientException e) {
            throw new ImageStoreException();
        } finally {
            removeFile(file);
        }
    }

    private File convertToFile(MultipartFile multipartFile) {
        String tempFilePath = getTempFilePath(multipartFile);
        File file = new File(tempFilePath);
        try {
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            if (file.exists()) {
                throw new TemporaryFileException(TRANSFER_ERROR);
            }
            throw new TemporaryFileException(CREATE_ERROR);
        }
    }

    private String getTempFilePath(MultipartFile multipartFile) {
        return this.tempFilePath + multipartFile.getOriginalFilename() + System.currentTimeMillis();
    }

    private String putImageToS3(File file, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        boolean res = file.delete();
        if (!res) {
            log.warn("파일이 삭제되지 않았습니다.");
        }
    }
}
