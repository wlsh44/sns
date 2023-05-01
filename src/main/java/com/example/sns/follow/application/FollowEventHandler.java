package com.example.sns.follow.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.common.infrastructure.fcm.dto.AlarmTargetDto;
import com.example.sns.common.infrastructure.fcm.dto.MessageDto;
import com.example.sns.follow.application.FollowedEvent;
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
public class FollowEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async("asyncThreadPoolTaskExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendFollowedAlarm(FollowedEvent event) {
        AlarmTargetDto target = event.getTarget();
        String text = FOLLOW.getText(event.getFollowerNickname());

        alarmRepository.save(Alarm.createFollowedAlarm(target.getTargetId(), text));

        List<MessageDto> messageDtoList = createMessageDtoList(target.getTargetDeviceToken(), text);
        alarmService.sendAll(messageDtoList);
    }

    private List<MessageDto> createMessageDtoList(List<String> tokens, String text) {
        return tokens.stream()
                .map(token -> new MessageDto(token, text, AlarmType.FOLLOW.name()))
                .toList();
    }
}
