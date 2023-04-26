package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
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
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAlarm(FollowedEvent event) {
        Member target = event.getFollowing();
        alarmService.send(target.getDeviceToken());
        alarmRepository.save(Alarm.createFollowedAlarm(target, event.getFollower()));
    }
}
