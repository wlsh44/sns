package com.example.sns.member.presentation;

import com.example.sns.common.infrastructure.imagestore.exception.ImageStoreException;
import com.example.sns.common.infrastructure.imagestore.exception.InvalidImageException;
import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.AlreadyExistNicknameException;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import com.example.sns.member.presentation.dto.MemberSearchDto;
import com.example.sns.member.presentation.dto.MemberSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_MEMBER_UPDATE_MULTIPART;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE1;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE_IMAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends MockControllerTest {

    @Test
    @DisplayName("프로필 조회에 성공할 경우 올바른 데이터와 200 응답을 줘야 함")
    void getProfileTest() throws Exception {
        //given
        MemberProfileResponse response = new MemberProfileResponse(1L, BASIC_NAME, BASIC_NICKNAME, null, null, 0, 0, false);
        given(memberQueryService.getProfile(any(), any()))
                .willReturn(response);

        //when then
        mockMvc.perform(get("/members/profile")
                        .param("username", "username")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 멤버의 프로필을 조회할 경우 400 응답을 줘야 함")
    void getProfileTest_memberNotFound() throws Exception {
        //given
        given(memberQueryService.getProfile(any(), any()))
                .willThrow(new MemberNotFoundException(1L));

        //when then
        mockMvc.perform(get("/members/profile")
                        .param("username", "username")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버 정보 조회에 성공할 경우 올바른 데이터와 200 응답을 줘야 함")
    void getMemberInfoTest() throws Exception {
        //given
        MemberInfoResponse response = new MemberInfoResponse(1L, BASIC_NAME, BASIC_NICKNAME, null, null, BASIC_EMAIL);
        given(memberQueryService.getMemberInfo(any()))
                .willReturn(response);

        //when then
        mockMvc.perform(get("/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 멤버의 정보를 조회할 경우 400 응답을 줘야 함")
    void getMemberInfoTest_memberNotFound() throws Exception {
        //given
        given(memberQueryService.getMemberInfo(any()))
                .willThrow(new MemberNotFoundException(1L));

        //when then
        mockMvc.perform(get("/members/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버 정보 수정에 성공할 경우 200 응답을 줘야 함")
    void updateMember() throws Exception {
        //given

        //when then
        mockMvc.perform(multipart("/members/me")
                        .file(BASIC_PROFILE_IMAGE)
                        .file(BASIC_MEMBER_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 멤버의 정보를 수정하면 예외가 발생해야 함")
    void updateMember_memberNotFound() throws Exception {
        //given
        doThrow(new MemberNotFoundException(1L))
                .when(memberCommandService).updateMember(any(), any(), any());

        //when then
        mockMvc.perform(multipart("/members/me")
                        .file(BASIC_PROFILE_IMAGE)
                        .file(BASIC_MEMBER_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이미 존재하는 닉네임으로 수정할 경우 예외가 발생해야 함")
    void updateMemberTest_alreadyExistNickname() throws Exception {
        //given
        doThrow(new AlreadyExistNicknameException(BASIC_NICKNAME2))
                .when(memberCommandService).updateMember(any(), any(), any());

        //when then
        mockMvc.perform(multipart("/members/me")
                        .file(BASIC_PROFILE_IMAGE)
                        .file(BASIC_MEMBER_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("프로필 저장에 실패할 경우 예외가 발생해야 함")
    void updateMemberTest_imageStoreException() throws Exception {
        //given
        doThrow(new ImageStoreException()).when(memberCommandService)
                .updateMember(any(), any(), any());

        //when then
        mockMvc.perform(multipart("/members/me")
                        .file(BASIC_PROFILE_IMAGE)
                        .file(BASIC_MEMBER_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("아미지가 없을 경우 예외가 발생해야 함")
    void updateMemberTest_invalidImage() throws Exception {
        //given
        doThrow(new InvalidImageException()).when(memberCommandService)
                .updateMember(any(), any(), any());

        //when then
        mockMvc.perform(multipart("/members/me")
                        .file(BASIC_PROFILE_IMAGE)
                        .file(BASIC_MEMBER_UPDATE_MULTIPART)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("닉네임으로 검색에 성공할 경우 올바른 데이터와 200 응답을 줘야 함")
    void searchMembers() throws Exception {
        //given
        MemberSearchResponse response = new MemberSearchResponse(List.of(new MemberSearchDto(1L, BASIC_NICKNAME, BASIC_PROFILE1)), true, 0);
        given(memberQueryService.searchMembers(any(), any()))
                .willReturn(response);

        //when then
        mockMvc.perform(get("/members/search")
                        .param("nickname", "nickname")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}