package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlreadyExistUsernameException extends SnsException {

    public static final String ERROR_MSG = "이미 존재하는 유저 이름입니다. username = %s";

    public AlreadyExistUsernameException(String username) {
        super(String.format(ERROR_MSG, username), HttpStatus.BAD_REQUEST);
    }
}
