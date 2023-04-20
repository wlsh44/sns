package com.example.sns.common.fixtures;

import com.example.sns.post.application.dto.PostUpdateRequest;
import com.example.sns.post.application.dto.PostUploadRequest;
import com.example.sns.post.domain.Post;
import com.example.sns.member.domain.Member;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostFixture {

    public static final String POST_IMAGE_PATH1 = "imagePath1";
    public static final String POST_IMAGE_PATH2 = "imagePath2";
    public static final String BASIC_POST_CONTENT = "post content";
    public static final String BASIC_POST_CONTENT2 = "post content2";
    public static final String EDIT_POST_CONTENT = "new post content";
    public static final String FILE_NAME1 = "test1.jpeg";
    public static final String FILE_NAME2 = "test2.jpeg";
    public static final MockMultipartFile BASIC_POST_IMAGE1 = new MockMultipartFile("postImages", FILE_NAME1, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_POST_IMAGE2 = new MockMultipartFile("postImages", FILE_NAME2, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_POST_UPLOAD_MULTIPART = new MockMultipartFile("dto", "", "application/json", "{\"content\": \"feed content\"}".getBytes());
    public static final MockMultipartFile BASIC_POST_UPDATE_MULTIPART = new MockMultipartFile("dto", "", "application/json", "{\"content\": \"new feed content\"}".getBytes());

    public static PostUploadRequest getBasicUploadRequest() {
        return new PostUploadRequest(BASIC_POST_CONTENT);
    }
    public static PostUpdateRequest getBasicUpdateRequest() {
        return new PostUpdateRequest(EDIT_POST_CONTENT);
    }

    public static List<MultipartFile> getBasicPostImages() {
        return List.of(BASIC_POST_IMAGE1, BASIC_POST_IMAGE2);
    }

    public static Post getBasicPost(Member member) {
        return Post.createPost(member, BASIC_POST_CONTENT);
    }

    public static Post getBasicPost2(Member member) {
        return Post.createPost(member, BASIC_POST_CONTENT2);
    }
}
