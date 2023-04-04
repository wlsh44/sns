package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.common.exception.dto.ErrorResponse;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;


import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_IMAGE1;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_IMAGE2;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPDATE_REQUEST_MULTIPART;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPLOAD_REQUEST_MULTIPART;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends MockControllerTest {

    @Test
    @DisplayName("피드 업로드를 성공하면 200 응답을 함")
    void uploadTest() throws Exception {
        mockMvc.perform(multipart("/posts")
                        .file(BASIC_POST_IMAGE1)
                        .file(BASIC_POST_IMAGE2)
                        .file(BASIC_POST_UPLOAD_REQUEST_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드를 수정하면 200 응답을 함")
    void updateTest() throws Exception {
        mockMvc.perform(multipart("/posts/1")
                        .file(BASIC_POST_IMAGE1)
                        .file(BASIC_POST_UPDATE_REQUEST_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 삭제에 성공하면 200 응답을 함")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("피드 삭제에 성공하면 200 응답을 함")
    void deleteTest_feedNotFound() throws Exception {
        //given
        ErrorResponse expect = new ErrorResponse(String.format(PostNotFoundException.ERROR_MSG, 1L));
        doThrow(new PostNotFoundException(1L))
                .when(postService)
                .deletePost(anyLong(), anyLong());

        mockMvc.perform(delete("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }
}