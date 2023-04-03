package com.example.sns.common.fixtures;

import com.example.sns.post.application.dto.PostUpdateRequest;
import com.example.sns.post.application.dto.PostUploadRequest;
import com.example.sns.post.domain.Post;
import com.example.sns.member.domain.Member;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FeedFixture {

    public static final String FEED_IMAGE_PATH1 = "imagePath1";
    public static final String FEED_IMAGE_PATH2 = "imagePath2";
    public static final String BASIC_FEED_CONTENT = "feed content";
    public static final String EDIT_FEED_CONTENT = "new feed content";
    public static final String FILE_NAME1 = "test1.jpeg";
    public static final String FILE_NAME2 = "test2.jpeg";
    public static final MockMultipartFile BASIC_FEED_IMAGE1 = new MockMultipartFile("feedImages", FILE_NAME1, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_FEED_IMAGE2 = new MockMultipartFile("feedImages", FILE_NAME2, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_FEED_UPLOAD_REQUEST_MULTIPART = new MockMultipartFile("dto", "", "application/json", "{\"content\": \"feed content\"}".getBytes());
    public static final MockMultipartFile BASIC_FEED_UPDATE_REQUEST_MULTIPART = new MockMultipartFile("dto", "", "application/json", "{\"content\": \"new feed content\"}".getBytes());

    public static PostUploadRequest getBasicUploadRequest() {
        return new PostUploadRequest(BASIC_FEED_CONTENT);
    }
    public static PostUpdateRequest getBasicUpdateRequest() {
        return new PostUpdateRequest(EDIT_FEED_CONTENT);
    }

    public static List<MultipartFile> getBasicFeedImages() {
        return List.of(BASIC_FEED_IMAGE1, BASIC_FEED_IMAGE2);
    }

    public static Post getBasicFeed(Member member) {
        return Post.createFeed(member, BASIC_FEED_CONTENT);
    }
}
