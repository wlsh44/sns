package com.example.sns.alarm.ui.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AlarmDto {

    private final Long alarmId;
    private final String text;
    private final boolean read;
}
