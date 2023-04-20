package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;


import java.time.LocalDateTime;
import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_IMAGE1;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_IMAGE2;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPDATE_MULTIPART;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_UPLOAD_MULTIPART;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends MockControllerTest {

    @Test
    @DisplayName("게시글 업로드를 성공하면 200 응답을 함")
    void uploadTest() throws Exception {
        mockMvc.perform(multipart("/posts")
                        .file(BASIC_POST_IMAGE1)
                        .file(BASIC_POST_IMAGE2)
                        .file(BASIC_POST_UPLOAD_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글를 수정하면 200 응답을 함")
    void updateTest() throws Exception {
        mockMvc.perform(multipart("/posts/1")
                        .file(BASIC_POST_IMAGE1)
                        .file(BASIC_POST_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 삭제에 성공하면 200 응답을 함")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 삭제에 성공하면 200 응답을 함")
    void deleteTest_feedNotFound() throws Exception {
        //given
        doThrow(new PostNotFoundException(1L))
                .when(postCommandService)
                .deletePost(anyLong(), anyLong());

        mockMvc.perform(delete("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 조회에 성공하면 데이터와 200 응답을 함")
    void findPostTest() throws Exception {
        //given
        PostResponse response = new PostResponse(1L, 1L, BASIC_NICKNAME, BASIC_PROFILE, List.of("url"), 0, BASIC_POST_CONTENT, LocalDateTime.now(), false);
        given(postQueryService.findPost(any(), any()))
                .willReturn(response);

        mockMvc.perform(get("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("유저가 없을 때 게시글 조회를 하는 경우 400 응답을 함")
    void findPostTest_memberNotFound() throws Exception {
        //given
        given(postQueryService.findPost(any(), any()))
                .willThrow(new MemberNotFoundException(1L));

        mockMvc.perform(get("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글이 없을 때 없을 때 게시글 조회를 하는 경우 400 응답을 함")
    void findPostTest_postNotFound() throws Exception {
        //given
        given(postQueryService.findPost(any(), any()))
                .willThrow(new PostNotFoundException(1L));

        mockMvc.perform(get("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }
}