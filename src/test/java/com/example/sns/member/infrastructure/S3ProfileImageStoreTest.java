package com.example.sns.member.infrastructure;

import com.example.sns.common.imagestore.ImageStore;
import com.example.sns.common.imagestore.exception.InvalidImageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPLOAD_MULTIPART;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class S3ProfileImageStoreTest {

    @InjectMocks
    S3ProfileImageStore profileImageStore;

    @Mock
    ImageStore imageStore;

    @Test
    @DisplayName("이미지가 s3에 저장되면 이미지 주소를 리턴해야 함")
    void saveProfileImage() throws Exception {
        //given
        String expectPath = "expect Path";
        given(imageStore.saveImage(any(), any()))
                .willReturn(expectPath);

        //when
        String imagePath = profileImageStore.saveProfileImage(BASIC_POST_UPLOAD_MULTIPART);

        //then
        assertThat(imagePath).isEqualTo(expectPath);
    }

    @Test
    @DisplayName("이미지가 없으면 예외처리를 해야 함")
    void saveProfileImage_validateEmptyImages() throws Exception {
        //given
        MultipartFile image = mock(MultipartFile.class);
        given(image.isEmpty())
                .willReturn(true);

        //when then
        assertThatThrownBy(() -> profileImageStore.saveProfileImage(image))
                .isInstanceOf(InvalidImageException.class);
    }
}