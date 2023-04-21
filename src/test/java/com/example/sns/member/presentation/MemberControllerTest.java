package com.example.sns.member.presentation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("예외가 발생할 경우 400 응답을 줘야 함")
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
}