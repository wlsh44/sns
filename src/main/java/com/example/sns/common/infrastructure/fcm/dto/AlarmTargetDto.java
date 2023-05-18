package com.example.sns.common.infrastructure.fcm.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AlarmTargetDto {

    private final Long targetId;
    private final List<String> targetDeviceToken;
}