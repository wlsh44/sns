package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.presentation.dto.AlarmDto;
import com.example.sns.alarm.presentation.dto.AlarmListResponse;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sns.common.fixtures.AlarmFixture.getFollowAlarm;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static org.assertj.core.api.Assertions.assertThat;

class AlarmQueryServiceTest extends ServiceTest {

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    AlarmQueryService alarmQueryService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("알람을 조회해야 함")
    void getAlarmsTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Alarm alarm = alarmRepository.save(getFollowAlarm(member));
        Pageable pageable = PageRequest.of(0, 10);
        AlarmListResponse expect = new AlarmListResponse(
                List.of(new AlarmDto(alarm.getId(), alarm.getText(), alarm.isRead())),
                true,
                0);

        //when
        AlarmListResponse response = alarmQueryService.findAlarms(member.getId(), pageable);

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }
}