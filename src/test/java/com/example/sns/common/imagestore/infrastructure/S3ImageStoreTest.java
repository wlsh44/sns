package com.example.sns.common.imagestore.infrastructure;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.example.sns.common.imagestore.exception.ImageStoreException;
import com.example.sns.common.imagestore.exception.TemporaryFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPLOAD_MULTIPART;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class S3ImageStoreTest {

    @InjectMocks
    S3ImageStore imageStore;

    @Mock
    AmazonS3 amazonS3;

    @Mock
    FileNameGenerator fileNameGenerator;

    @Test
    @DisplayName("s3에 이미지를 저장하면 경로를 리턴해야 함")
    void saveImage() throws Exception {
        //given
        String expect = "http://imagePath.com";
        given(fileNameGenerator.generateFileName(any(), any()))
                .willReturn(expect);
        given(amazonS3.getUrl(any(), any()))
                .willReturn(new URL(expect));

        //when
        String res = imageStore.saveImage(BASIC_POST_UPLOAD_MULTIPART, "imagePath");

        //then
        assertThat(res).isEqualTo(expect);
    }

    @Test
    @DisplayName("s3 저장에 실패한 경우 예외가 발생해야 함")
    void saveImage_imageStoreException() throws Exception {
        //given
        doThrow(SdkClientException.class).when(amazonS3)
                .putObject(any());

        //when then
        assertThatThrownBy(() -> imageStore.saveImage(BASIC_POST_UPLOAD_MULTIPART, "imagePath"))
                .isInstanceOf(ImageStoreException.class);
    }

    @Test
    @DisplayName("임시 파일 저장에 실패한 경우 예외가 발생해야 함")
    void saveImage_temporaryFileException() throws Exception {
        //given
        MultipartFile multipart = mock(MultipartFile.class);
        doThrow(IOException.class).when(multipart)
                        .transferTo(any(File.class));

        //when then
        assertThatThrownBy(() -> imageStore.saveImage(multipart, "imagePath"))
                .isInstanceOf(TemporaryFileException.class);
    }
}