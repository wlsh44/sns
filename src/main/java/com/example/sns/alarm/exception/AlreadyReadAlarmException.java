package com.example.sns.alarm.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlreadyReadAlarmException extends SnsException {

    public static final String ERROR_MSG = "이미 읽은 알람입니다.";

    public AlreadyReadAlarmException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
