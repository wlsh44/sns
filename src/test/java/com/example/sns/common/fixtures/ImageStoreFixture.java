package com.example.sns.common.fixtures;

import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageStoreFixture {

    public static final String FEED_STORE_PATH = "src/test/resources/image/feed/";
    public static final String PROFILE_STORE_PATH = "src/test/resources/image/profile/";
    public static final String FILE_NAME1 = "cat";
    public static final String FILE_NAME2 = "dog";
    public static final String EXT = "png";
    public static final MockMultipartFile BASIC_FEED_IMAGE1;
    public static final MockMultipartFile BASIC_FEED_IMAGE2;

    static {
        try {
            BASIC_FEED_IMAGE1 = new MockMultipartFile(
                    FILE_NAME1,
                    FILE_NAME1 + "." + EXT,
                    EXT,
                    new FileInputStream(FEED_STORE_PATH + FILE_NAME1 + "." + EXT)
            );
            BASIC_FEED_IMAGE2 = new MockMultipartFile(
                    FILE_NAME2,
                    FILE_NAME2 + "." + EXT,
                    EXT,
                    new FileInputStream(FEED_STORE_PATH + FILE_NAME2 + "." + EXT)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
