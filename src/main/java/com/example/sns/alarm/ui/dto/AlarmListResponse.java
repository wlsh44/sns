package com.example.sns.alarm.ui.dto;

import com.example.sns.alarm.domain.Alarm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AlarmListResponse {

    private final List<AlarmDto> alarms;
    private final boolean last;
    private final int offset;

    public static AlarmListResponse createResponse(List<Alarm> alarms, boolean hasNext, int offset) {
        List<AlarmDto> alarmDtoList = alarms.stream()
                .map(alarm -> new AlarmDto(alarm.getId(), alarm.getText(), alarm.isRead()))
                .toList();
        return new AlarmListResponse(alarmDtoList, !hasNext, offset);
    }
}
