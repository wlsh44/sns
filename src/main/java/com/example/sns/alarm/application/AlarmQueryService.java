package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.ui.dto.AlarmListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmRepository alarmRepository;

    public AlarmListResponse findAlarms(Long memberId, Pageable pageable) {
        Slice<Alarm> alarmSlice = alarmRepository.findByMemberId(memberId, pageable);

        return AlarmListResponse.createResponse(alarmSlice.getContent(), alarmSlice.hasNext(), alarmSlice.getNumber());
    }
}
