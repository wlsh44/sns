package com.example.sns.post.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.domain.AlarmType;
import com.example.sns.common.infrastructure.fcm.AlarmService;
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

@Component
@RequiredArgsConstructor
public class PostUploadEventHandler {

    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @Async("asyncThreadPoolTaskExecutor")
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPostUploadedAlarm(PostUploadedEvent event) {
        List<Member> targets = event.getTargets();
        Member postAuthor = event.getPostAuthor();

        List<Alarm> alarms = alarmRepository.saveAll(createUploadAlarms(targets, postAuthor));

        List<MessageDto> messageDtoList = getMessageDtoList(alarms);
        alarmService.sendAll(messageDtoList);
    }

    private List<Alarm> createUploadAlarms(List<Member> targets, Member postAuthor) {
        return targets.stream()
                .map(target -> Alarm.createPostUploadedAlarm(target, postAuthor))
                .toList();
    }

    private List<MessageDto> getMessageDtoList(List<Alarm> alarms) {
        return alarms.stream()
                .map(alarm -> createMessageDtoList(alarm.getTarget(), alarm.getText()))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<MessageDto> createMessageDtoList(Member target, String text) {
        return target.getDeviceTokens().stream()
                .map(token -> new MessageDto(token, text, AlarmType.POST_UPLOAD.name()))
                .toList();
    }
}
