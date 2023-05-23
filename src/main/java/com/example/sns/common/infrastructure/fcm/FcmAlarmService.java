package com.example.sns.common.infrastructure.fcm;

import com.example.sns.common.infrastructure.fcm.dto.MessageDto;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAlarmService implements AlarmService {

    private static final String TIME = "time";
    private static final String TYPE = "type";
    private static final String TITLE = "Sns Alarm";

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendAll(List<MessageDto> messageDtoList) {
        if (messageDtoList.isEmpty()) {
            return;
        }
        List<Message> messages = createMessages(messageDtoList);
        sendMessages(messages)
                .filter(this::isFailureExist)
                .map(BatchResponse::getResponses)
                .ifPresent(sendResponses -> logFailedMessages(sendResponses, messageDtoList));
    }

    private List<Message> createMessages(List<MessageDto> messageDtoList) {
        return messageDtoList.stream()
                .map(this::createMessage)
                .toList();
    }

    private Message createMessage(MessageDto messageDto) {
        return Message.builder()
                .putData(TIME, LocalDateTime.now().toString())
                .putData(TYPE, messageDto.getType())
                .setNotification(Notification.builder().setTitle(TITLE).setBody(messageDto.getText()).build())
                .setToken(messageDto.getToken())
                .build();
    }

    private Optional<BatchResponse> sendMessages(List<Message> messages) {
        try {
            return Optional.of(firebaseMessaging.sendAll(messages));
        } catch (FirebaseMessagingException e) {
            log.warn("푸쉬 알림에 실패했습니다. e = {}", e.getMessage());
            return Optional.empty();
        }
    }

    private boolean isFailureExist(BatchResponse batchResponse) {
        return batchResponse.getFailureCount() > 0;
    }

    private void logFailedMessages(List<SendResponse> responses, List<MessageDto> messageDtoList) {
        List<FailedMessageInfo> failedMessageInfoList = new ArrayList<>();

        for (int i = 0; i < messageDtoList.size(); i++) {
            final String token = messageDtoList.get(i).getToken();

            Optional.of(responses.get(i))
                    .filter(sendResponse -> !sendResponse.isSuccessful())
                    .map(sendResponse -> new FailedMessageInfo(sendResponse.getMessageId(), token))
                    .ifPresent(failedMessageInfoList::add);
        }
        log.warn("올바르지 않은 FCM 토큰이 존재합니다. tokens = " + failedMessageInfoList);
    }

    @ToString
    @AllArgsConstructor
    private static class FailedMessageInfo {
        private String failedToken;
        private String failedMessageId;
    }
}
