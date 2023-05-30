package com.example.sns.like.presentation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.exception.AlreadyLikedPostException;
import com.example.sns.post.exception.NotLikedPostException;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LikeControllerTest extends MockControllerTest {

    @Test
    @DisplayName("좋아요에 성공하면 200 응답을 해야 함")
    void likeTest() throws Exception {
        //given
        doNothing().when(likeService).like(any(), any());

        //when then
        mockMvc.perform(post("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저인 경우 좋아요를 할 때 400 응답을 해야 함")
    void likeTest_memberNotFound() throws Exception {
        //given
        doThrow(new MemberNotFoundException(1L)).when(likeService).like(any(), any());

        //when then
        mockMvc.perform(post("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("없는 게시글인 경우 좋아요를 할 때 400 응답을 해야 함")
    void likeTest_postNotFound() throws Exception {
        //given
        doThrow(new PostNotFoundException(1L)).when(likeService).like(any(), any());

        //when then
        mockMvc.perform(post("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이미 좋아요를 한 게시글인 경우 좋아요를 할 때 400 응답을 해야 함")
    void likeTest_alreadyLikedPost() throws Exception {
        //given
        doThrow(new AlreadyLikedPostException(1L, 1L)).when(likeService).like(any(), any());

        //when then
        mockMvc.perform(post("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 취소에 성공하면 200 응답을 해야 함")
    void cancelLikeTest() throws Exception {
        //given
        doNothing().when(likeService).cancelLike(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저인 경우 좋아요 취소를 할 때 400 응답을 해야 함")
    void cancelLikeTest_memberNotFound() throws Exception {
        //given
        doThrow(new MemberNotFoundException(1L)).when(likeService).cancelLike(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("없는 게시글인 경우 좋아요 취소를 할 때 400 응답을 해야 함")
    void cancelLikeTest_postNotFound() throws Exception {
        //given
        doThrow(new PostNotFoundException(1L)).when(likeService).cancelLike(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요를 하지 않은 게시글인 경우 좋아요 취소를 할 때 400 응답을 해야 함")
    void cancelLikeTest_alreadyLikedPost() throws Exception {
        //given
        doThrow(new NotLikedPostException()).when(likeService).cancelLike(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/like")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }
}