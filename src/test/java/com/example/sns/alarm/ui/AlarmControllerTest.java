package com.example.sns.alarm.ui;

import com.example.sns.alarm.exception.AlarmNotFoundException;
import com.example.sns.alarm.exception.AlreadyReadAlarmException;
import com.example.sns.alarm.exception.NotAlarmReceiverException;
import com.example.sns.alarm.ui.dto.AlarmDto;
import com.example.sns.alarm.ui.dto.AlarmListResponse;
import com.example.sns.common.support.MockControllerTest;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import com.example.sns.post.presentiation.dto.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE1;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlarmControllerTest extends MockControllerTest {

    @Test
    @DisplayName("알람 조회에 성공하면 데이터와 200 응답을 함")
    void findAlarmsTest() throws Exception {
        //given
        AlarmListResponse response = new AlarmListResponse(
                List.of(new AlarmDto(1L, "text", false)), true, 0);
        given(alarmQueryService.findAlarms(any(), any()))
                .willReturn(response);

        mockMvc.perform(get("/alarms")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("알람 읽기에 성공하면 200 응답을 함")
    void readAlarmTest() throws Exception {

        //when then
        mockMvc.perform(post("/alarms/1/read")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 알람을 읽으면 400 응답을 함")
    void readAlarmTest_alarmNotFound() throws Exception {
        //given
        doThrow(new AlarmNotFoundException(1L))
                .when(alarmCommandService)
                .readAlarm(any(), any());

        //when then
        mockMvc.perform(post("/alarms/1/read")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("안 받은 알람을 읽으면 400 응답을 함")
    void readAlarmTest_notAlarmReceiver() throws Exception {
        //given
        doThrow(new NotAlarmReceiverException())
                .when(alarmCommandService)
                .readAlarm(any(), any());

        //when then
        mockMvc.perform(post("/alarms/1/read")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("읽은 알람을 읽으면 400 응답을 함")
    void readAlarmTest_alreadyReadAlarm() throws Exception {
        //given
        doThrow(new AlreadyReadAlarmException())
                .when(alarmCommandService)
                .readAlarm(any(), any());

        //when then
        mockMvc.perform(post("/alarms/1/read")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }
}