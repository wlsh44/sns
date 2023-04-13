package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends SnsException {

    public static final String ERROR_MSG_LONG = "해당 유저가 존재하지 않습니다. id = %d";
    public static final String ERROR_MSG_STRING = "해당 유저가 존재하지 않습니다. id = %s";

    public MemberNotFoundException(Long id) {
        super(String.format(ERROR_MSG_LONG, id), HttpStatus.BAD_REQUEST);
    }

    public MemberNotFoundException(String nickname) {
        super(String.format(ERROR_MSG_STRING, nickname), HttpStatus.BAD_REQUEST);
    }
}
