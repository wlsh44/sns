package com.example.sns.post.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.common.infrastructure.fcm.dto.AlarmTargetsDto;
import com.example.sns.common.infrastructure.fcm.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static com.example.sns.alarm.domain.AlarmType.POST_UPLOAD;

@Component
@RequiredArgsConstructor
public class PostUploadEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener
    public void sendPostUploadedAlarm(PostUploadedEvent event) {
        AlarmTargetsDto targets = event.getTargets();
        String text = POST_UPLOAD.getText(event.getAuthorUsername());

        alarmRepository.saveAll(Alarm.createPostUploadedAlarms(targets.getTargetIds(), text));

        List<MessageDto> messageDtoList = createMessageDtoList(targets.getTargetDeviceTokens(), text);
        alarmService.sendAll(messageDtoList);
    }

    private List<MessageDto> createMessageDtoList(List<String> tokens, String text) {
        return tokens.stream()
                .map(token -> new MessageDto(token, text, AlarmType.POST_UPLOAD.name()))
                .toList();
    }
}
