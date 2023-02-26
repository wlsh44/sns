package com.example.sns.social.presentation;

import com.example.sns.common.controller.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.social.application.dto.FollowRequest;
import com.example.sns.social.application.dto.UnfollowRequest;
import com.example.sns.social.exception.AlreadyFollowException;
import com.example.sns.social.exception.NotFollowingMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.rmi.AlreadyBoundException;

import static com.example.sns.common.fixtures.SocialFixture.getFollowRequest;
import static com.example.sns.common.fixtures.SocialFixture.getUnfollowRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SocialControllerTest extends MockControllerTest {

    @Test
    @DisplayName("팔로우를 성공하면 200 응답코드를 리턴함")
    void follow() throws Exception {
        //given
        FollowRequest request = getFollowRequest();

        //when then
        mockMvc.perform(post("/social/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저를 팔로우할 경우 400 응답 코드를 리턴함")
    void follow_memberNotFound() throws Exception {
        //given
        Long memberId = 1L;
        FollowRequest request = getFollowRequest();
        given(jwtProvider.parseMemberId(any()))
                .willReturn(memberId);
        doThrow(new MemberNotFoundException(memberId))
                .when(socialService).follow(eq(memberId), any(FollowRequest.class));

        //when then
        mockMvc.perform(post("/social/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("없는 유저를 팔로우할 경우 400 응답 코드를 리턴함")
    void follow_alreadyFollow() throws Exception {
        //given
        Long memberId = 1L;
        FollowRequest request = getFollowRequest();
        given(jwtProvider.parseMemberId(any()))
                .willReturn(memberId);
        doThrow(new AlreadyFollowException())
                .when(socialService).follow(eq(memberId), any(FollowRequest.class));

        //when then
        mockMvc.perform(post("/social/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("언팔로우를 성공하면 200 응답코드를 리턴함")
    void unfollow() throws Exception {
        //given
        UnfollowRequest request = getUnfollowRequest();

        //when then
        mockMvc.perform(post("/social/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저를 언팔로우할 경우 400 응답 코드를 리턴함")
    void unfollow_memberNotFound() throws Exception {
        //given
        Long memberId = 1L;
        UnfollowRequest request = getUnfollowRequest();
        given(jwtProvider.parseMemberId(any()))
                .willReturn(memberId);
        doThrow(new MemberNotFoundException(memberId))
                .when(socialService).unfollow(eq(memberId), any(UnfollowRequest.class));

        //when then
        mockMvc.perform(post("/social/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팔로우 하지 않은 유저를 언팔로우할 경우 400 응답 코드를 리턴함")
    void unfollow_notFollowingMember() throws Exception {
        //given
        Long memberId = 1L;
        UnfollowRequest request = getUnfollowRequest();
        given(jwtProvider.parseMemberId(any()))
                .willReturn(memberId);
        doThrow(new NotFollowingMemberException())
                .when(socialService).unfollow(eq(memberId), any(UnfollowRequest.class));

        //when then
        mockMvc.perform(post("/social/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}