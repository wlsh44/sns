package com.example.sns.post.infrastructure;

import com.example.sns.common.infrastructure.imagestore.ImageStore;
import com.example.sns.common.infrastructure.imagestore.exception.InvalidImageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPLOAD_MULTIPART;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class S3PostImageStoreTest {

    @InjectMocks
    S3PostImageStore s3PostImageStore;

    @Mock
    ImageStore imageStore;

    @Test
    @DisplayName("이미지가 s3에 저장되면 이미지 주소를 리턴해야 함")
    void savePostImages() throws Exception {
        //given
        String expectPath = "expect Path";
        List<MultipartFile> images = List.of(BASIC_POST_UPLOAD_MULTIPART);
        given(imageStore.saveImage(any(), any()))
                .willReturn(expectPath);

        //when
        List<String> imagePaths = s3PostImageStore.savePostImages(images);

        //then
        assertThat(imagePaths).hasSize(1);
        assertThat(imagePaths.get(0)).isEqualTo(expectPath);
    }

    @Test
    @DisplayName("이미지가 없으면 예외처리를 해야 함")
    void savePostImages_validateEmptyImages() throws Exception {
        //given
        MultipartFile multipart = mock(MultipartFile.class);
        List<MultipartFile> images = List.of(multipart);
        given(multipart.isEmpty())
                .willReturn(true);

        //when then
        assertThatThrownBy(() -> s3PostImageStore.savePostImages(images))
                .isInstanceOf(InvalidImageException.class);
    }
}