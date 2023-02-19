package com.example.sns.feed.presentiation;

import com.example.sns.common.controller.MockControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_IMAGE1;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_IMAGE2;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_UPLOAD_REQUEST_MULTIPART;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedControllerTest extends MockControllerTest {

    @Test
    @DisplayName("파일 업로드에 성공하면 200 응답을 함")
    void uploadTest() throws Exception {
        mockMvc.perform(multipart("/feeds")
                        .file(BASIC_FEED_IMAGE1)
                        .file(BASIC_FEED_IMAGE2)
                        .file(BASIC_FEED_UPLOAD_REQUEST_MULTIPART))
                        .andDo(print())
                .andExpect(status().isOk());
    }

}