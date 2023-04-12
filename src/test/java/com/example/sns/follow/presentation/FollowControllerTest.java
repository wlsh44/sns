package com.example.sns.follow.presentation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.follow.application.dto.FollowRequest;
import com.example.sns.follow.application.dto.UnfollowRequest;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.SocialFixture.getFollowRequest;
import static com.example.sns.common.fixtures.SocialFixture.getUnfollowRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerTest extends MockControllerTest {

    @Test
    @DisplayName("팔로우를 성공하면 200 응답코드를 리턴함")
    void follow() throws Exception {
        //given
        FollowRequest request = getFollowRequest();

        //when then
        mockMvc.perform(post("/social/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
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
                .when(followService).follow(eq(memberId), any(FollowRequest.class));

        //when then
        mockMvc.perform(post("/social/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
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
                .when(followService).follow(eq(memberId), any(FollowRequest.class));

        //when then
        mockMvc.perform(post("/social/follow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
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
                .when(followService).unfollow(eq(memberId), any(UnfollowRequest.class));

        //when then
        mockMvc.perform(post("/social/unfollow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
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
                .when(followService).unfollow(eq(memberId), any(UnfollowRequest.class));

        //when then
        mockMvc.perform(post("/social/unfollow")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}