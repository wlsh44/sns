package com.example.sns.post.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.common.infrastructure.fcm.dto.AlarmTargetsDto;
import com.example.sns.common.infrastructure.fcm.dto.MessageDto;
import com.example.sns.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collection;
import java.util.List;

import static com.example.sns.alarm.domain.AlarmType.POST_UPLOAD;

@Component
@RequiredArgsConstructor
public class PostUploadEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPostUploadedAlarm(PostUploadedEvent event) {
        AlarmTargetsDto targets = event.getTargets();
        String text = POST_UPLOAD.getText(event.getAuthorNickname());

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
