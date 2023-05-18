package com.example.sns.follow.presentation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerTest extends MockControllerTest {

    @Test
    @DisplayName("팔로우를 성공하면 200 응답코드를 리턴함")
    void follow() throws Exception {
        //given

        //when then
        mockMvc.perform(post("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저를 팔로우할 경우 400 응답 코드를 리턴함")
    void follow_memberNotFound() throws Exception {
        //given
        doThrow(new MemberNotFoundException(1L))
                .when(followService).follow(any(), any());

        //when then
        mockMvc.perform(post("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("없는 유저를 팔로우할 경우 400 응답 코드를 리턴함")
    void follow_alreadyFollow() throws Exception {
        //given
        doThrow(new AlreadyFollowException())
                .when(followService).follow(any(), any());

        //when then
        mockMvc.perform(post("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("언팔로우를 성공하면 200 응답코드를 리턴함")
    void unfollow() throws Exception {
        //given

        //when then
        mockMvc.perform(delete("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저를 언팔로우할 경우 400 응답 코드를 리턴함")
    void unfollow_memberNotFound() throws Exception {
        //given
        doThrow(new MemberNotFoundException(1L))
                .when(followService).unfollow(any(), any());

        //when then
        mockMvc.perform(delete("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팔로우 하지 않은 유저를 언팔로우할 경우 400 응답 코드를 리턴함")
    void unfollow_notFollowingMember() throws Exception {
        //given
        doThrow(new NotFollowingMemberException())
                .when(followService).unfollow(any(), any());

        //when then
        mockMvc.perform(delete("/members/1/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }
}