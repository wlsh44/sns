package com.example.sns.alarm.domain;

import com.example.sns.alarm.exception.AlreadyReadAlarmException;
import com.example.sns.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.sns.common.fixtures.AlarmFixture.getFollowAlarm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AlarmTest {

    @Mock
    Member member;

    @Test
    @DisplayName("알람을 받은 유저가 아니면 false를 리턴해야 함")
    void isOwnerTest() throws Exception {
        //given
        given(member.getId())
                .willReturn(1L);
        Alarm alarm = getFollowAlarm(member);

        //when
        boolean res = alarm.isReceiver(2L);

        //then
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("알람을 읽어야 함")
    void readTest() throws Exception {
        //given
        Alarm alarm = getFollowAlarm(member);

        //when
        alarm.read();

        //then
        assertThat(alarm.isRead()).isTrue();
    }

    @Test
    @DisplayName("이미 읽은 알람을 읽는 경우 예외가 발생해야 함")
    void readTest_alreadyReadAlarm() throws Exception {
        //given
        Alarm alarm = getFollowAlarm(member);
        alarm.read();

        //when then
        assertThatThrownBy(alarm::read)
                .isInstanceOf(AlreadyReadAlarmException.class);
    }
}