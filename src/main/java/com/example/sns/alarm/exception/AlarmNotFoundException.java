package com.example.sns.alarm.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlarmNotFoundException extends SnsException {

    public static final String ERROR_MSG = "존재하지 않는 알람입니다. id = %d";

    public AlarmNotFoundException(Long alarmId) {
        super(String.format(ERROR_MSG, alarmId), HttpStatus.BAD_REQUEST);
    }
}
