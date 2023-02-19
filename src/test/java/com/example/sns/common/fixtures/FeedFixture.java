package com.example.sns.common.fixtures;

import com.example.sns.feed.application.dto.FeedUploadRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FeedFixture {

    public static final String FEED_IMAGE_PATH1 = "imagePath1";
    public static final String FEED_IMAGE_PATH2 = "imagePath2";
    public static final String BASIC_FEED_CONTENT = "feed content";
    public static final String FILE_NAME1 = "test1.jpeg";
    public static final String FILE_NAME2 = "test2.jpeg";
    public static final MockMultipartFile BASIC_FEED_IMAGE1 = new MockMultipartFile("feedImages", FILE_NAME1, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_FEED_IMAGE2 = new MockMultipartFile("feedImages", FILE_NAME2, "image/jpeg", "image".getBytes());
    public static final MockMultipartFile BASIC_FEED_UPLOAD_REQUEST_MULTIPART = new MockMultipartFile("dto", "", "application/json", "{\"content\": \"feed content\"}".getBytes());

    public static FeedUploadRequest getBasicUploadRequest() {
        return new FeedUploadRequest(BASIC_FEED_CONTENT);
    }

    public static List<MultipartFile> getBasicFeedImages() {
        return List.of(BASIC_FEED_IMAGE1, BASIC_FEED_IMAGE2);
    }
}
