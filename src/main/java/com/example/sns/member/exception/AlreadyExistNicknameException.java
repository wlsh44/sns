package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlreadyExistNicknameException extends SnsException {

    public static final String ERROR_MSG = "이미 존재하는 닉네입입니다. nickname = %s";

    public AlreadyExistNicknameException(String nickname) {
        super(String.format(ERROR_MSG, nickname), HttpStatus.BAD_REQUEST);
    }
}
