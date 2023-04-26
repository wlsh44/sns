package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.alarm.infrastructure.dto.MessageDto;
import com.example.sns.follow.application.FollowedEvent;
import com.example.sns.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static com.example.sns.alarm.domain.AlarmType.FOLLOW;

@Component
@RequiredArgsConstructor
public class AlarmEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAlarm(FollowedEvent event) {
        Member target = event.getFollowing();

        Alarm alarm = alarmRepository.save(Alarm.createFollowedAlarm(target, event.getFollower()));

        List<MessageDto> messageDtoList = createMessageDtoList(target, alarm.getText(), FOLLOW);
        alarmService.sendAll(messageDtoList);
    }

    private List<MessageDto> createMessageDtoList(Member target, String text, AlarmType alarmType) {
        return target.getDeviceTokens().stream()
                .map(token -> new MessageDto(token, text, alarmType.name()))
                .toList();
    }
}
