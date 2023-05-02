package com.example.sns.follow.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.application.FollowEventHandler;
import com.example.sns.follow.application.FollowService;
import com.example.sns.follow.application.FollowedEvent;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.sns.common.fixtures.MemberFixture.getFollower;
import static com.example.sns.common.fixtures.MemberFixture.getFollowing;
import static org.assertj.core.api.Assertions.assertThat;

@RecordApplicationEvents
class FollowEventHandlerTest extends ServiceTest {

    @Autowired
    FollowEventHandler alarmEventHandler;

    @MockBean
    AlarmService alarmService;

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowService followService;

    @Autowired
    ApplicationEvents events;

    @Autowired
    ThreadPoolTaskExecutor executor;

    @Test
    @DisplayName("팔로우를 하면 팔로우 알람 이벤트가 발생해야 함")
    void sendFollowedAlarmEventTest() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());

        //when
        followService.follow(follower.getId(), following.getId());
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        int count = (int) events.stream(FollowedEvent.class).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("팔로우 알람 이벤트가 발생하면 알람이 저장되어야 함")
    void sendFollowedAlarmEventTest_alarmSave() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());

        //when
        alarmEventHandler.sendFollowedAlarm(new FollowedEvent(follower, following));
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        List<Alarm> alarms = alarmRepository.findAll();
        assertThat(alarms).hasSize(1);
    }
}