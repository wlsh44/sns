package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.exception.AlarmNotFoundException;
import com.example.sns.alarm.exception.NotAlarmReceiverException;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.sns.common.fixtures.AlarmFixture.getFollowAlarm;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlarmCommandServiceTest extends ServiceTest {

    @Autowired
    AlarmCommandService alarmCommandService;

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("알람을 읽어야 함")
    void readTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Alarm alarm = alarmRepository.save(getFollowAlarm(member));

        //when
        alarmCommandService.readAlarm(member.getId(), alarm.getId());

        //then
        Alarm readAlarm = alarmRepository.findAll().get(0);
        assertThat(readAlarm.isRead()).isTrue();
    }

    @Test
    @DisplayName("없는 알람을 읽을 경우 예외가 발생해야 함")
    void readTest_alarmNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Long memberId = memberRepository.save(getBasicMember()).getId();

        //when then
        assertThatThrownBy(() -> alarmCommandService.readAlarm(memberId, notExistId))
                .isInstanceOf(AlarmNotFoundException.class);
    }

    @Test
    @DisplayName("알람을 안 받은 사람이 알람을 읽을 경우 예외가 발생해야 함")
    void readTest_notAlarmOwner() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Long alarmId = alarmRepository.save(getFollowAlarm(member)).getId();
        Long notAlarmReceiverId = memberRepository.save(getBasicMember2()).getId();

        //when then
        assertThatThrownBy(() -> alarmCommandService.readAlarm(notAlarmReceiverId, alarmId))
                .isInstanceOf(NotAlarmReceiverException.class);
    }
}