package com.example.sns.feed.presentiation;

import com.example.sns.common.controller.MockControllerTest;
import com.example.sns.common.exception.dto.ErrorResponse;
import com.example.sns.feed.exception.FeedNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_IMAGE1;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_IMAGE2;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_UPDATE_REQUEST_MULTIPART;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_UPLOAD_REQUEST_MULTIPART;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedControllerTest extends MockControllerTest {

    @Test
    @DisplayName("피드 업로드를 성공하면 200 응답을 함")
    void uploadTest() throws Exception {
        mockMvc.perform(multipart("/feeds")
                        .file(BASIC_FEED_IMAGE1)
                        .file(BASIC_FEED_IMAGE2)
                        .file(BASIC_FEED_UPLOAD_REQUEST_MULTIPART))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드를 수정하면 200 응답을 함")
    void updateTest() throws Exception {
        mockMvc.perform(multipart("/feeds/1")
                        .file(BASIC_FEED_IMAGE1)
                        .file(BASIC_FEED_UPDATE_REQUEST_MULTIPART))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 삭제에 성공하면 200 응답을 함")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/feeds/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 삭제에 성공하면 200 응답을 함")
    void deleteTest_feedNotFound() throws Exception {
        //given
        ErrorResponse expect = new ErrorResponse(String.format(FeedNotFoundException.ERROR_MSG, 1L));
        doThrow(new FeedNotFoundException(1L))
                .when(feedService)
                .deleteFeed(anyLong(), anyLong());

        mockMvc.perform(delete("/feeds/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }
}