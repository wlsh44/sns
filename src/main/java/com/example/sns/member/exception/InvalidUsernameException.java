package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class InvalidUsernameException extends SnsException {

    public static final String ERROR_MSG = "올바르지 않은 닉네임입니다. username = %s";

    public InvalidUsernameException(String username) {
        super(String.format(ERROR_MSG, username), HttpStatus.BAD_REQUEST);
    }
}
