package com.example.sns.member.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.common.infrastructure.fcm.dto.MessageDto;
import com.example.sns.follow.application.FollowedEvent;
import com.example.sns.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async("asyncThreadPoolTaskExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendFollowedAlarm(FollowedEvent event) {
        Member target = event.getFollowing();

        Alarm alarm = alarmRepository.save(Alarm.createFollowedAlarm(target, event.getFollower()));

        List<MessageDto> messageDtoList = createMessageDtoList(target, alarm.getText());
        alarmService.sendAll(messageDtoList);
    }

    private List<MessageDto> createMessageDtoList(Member target, String text) {
        return target.getDeviceTokens().stream()
                .map(token -> new MessageDto(token, text, AlarmType.FOLLOW.name()))
                .toList();
    }
}
