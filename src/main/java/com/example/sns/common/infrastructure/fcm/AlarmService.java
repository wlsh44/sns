package com.example.sns.common.infrastructure.fcm;

import com.example.sns.common.infrastructure.fcm.dto.MessageDto;

import java.util.List;

public interface AlarmService {
    void sendAll(List<MessageDto> messageDtoList);
}
