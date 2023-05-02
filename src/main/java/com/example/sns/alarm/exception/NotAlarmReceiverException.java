package com.example.sns.alarm.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class NotAlarmReceiverException extends SnsException {

    public static final String ERROR_MSG = "알람을 받은 유저가 아닙니다.";

    public NotAlarmReceiverException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
