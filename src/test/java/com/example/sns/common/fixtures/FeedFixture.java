package com.example.sns.common.fixtures;

import com.example.sns.feed.application.dto.FeedUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.sns.common.fixtures.ImageStoreFixture.BASIC_FEED_IMAGE1;
import static com.example.sns.common.fixtures.ImageStoreFixture.BASIC_FEED_IMAGE2;

public class FeedFixture {

    public static final String FEED_IMAGE_PATH1 = "imagePath1";
    public static final String FEED_IMAGE_PATH2 = "imagePath2";
    public static final String BASIC_FEED_CONTENT = "feed content";
    public static FeedUploadRequest getBasicUploadRequest() {
        return new FeedUploadRequest(BASIC_FEED_CONTENT);
    }

    public static List<MultipartFile> getBasicFeedImages() {
        return List.of(BASIC_FEED_IMAGE1, BASIC_FEED_IMAGE2);
    }
}
