package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.alarm.exception.AlarmNotFoundException;
import com.example.sns.alarm.exception.NotAlarmReceiverException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmCommandService {

    private final AlarmRepository alarmRepository;

    public void readAlarm(Long memberId, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new AlarmNotFoundException(alarmId));
        validateAlarmReceiver(memberId, alarm);

        alarm.read();
    }

    private void validateAlarmReceiver(Long memberId, Alarm alarm) {
        if (!alarm.isReceiver(memberId)) {
            throw new NotAlarmReceiverException();
        }
    }
}
