package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.FollowAlarm;
import com.example.sns.alarm.domain.FollowAlarmRepository;
import com.example.sns.follow.application.FollowedEvent;
import com.example.sns.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FollowAlarmEventHandler {

    private final AlarmService alarmService;
    private final FollowAlarmRepository followAlarmRepository;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAlarm(FollowedEvent event) {
        Member target = event.getFollower();
        alarmService.send(target.getDeviceToken());
        followAlarmRepository.save(new FollowAlarm(target, event.getFollowing()));
    }
}
