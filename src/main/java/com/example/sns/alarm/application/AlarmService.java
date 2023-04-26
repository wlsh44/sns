package com.example.sns.alarm.application;

import com.example.sns.alarm.infrastructure.dto.MessageDto;

import java.util.List;

public interface AlarmService {
    void sendAll(List<MessageDto> messageDtoList);
}
