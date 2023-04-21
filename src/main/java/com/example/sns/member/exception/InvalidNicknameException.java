package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class InvalidNicknameException extends SnsException {

    public static final String ERROR_MSG = "올바르지 않은 닉네임입니다. nickname = %s";

    public InvalidNicknameException(String nickname) {
        super(String.format(ERROR_MSG, nickname), HttpStatus.BAD_REQUEST);
    }
}
