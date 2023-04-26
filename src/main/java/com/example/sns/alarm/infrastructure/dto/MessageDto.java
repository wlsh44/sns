package com.example.sns.alarm.infrastructure.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageDto {

    private final String token;
    private final String text;
    private final String type;
}
