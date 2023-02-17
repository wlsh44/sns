package com.example.sns.auth.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends AuthException {

    public static final String ERROR_MSG = "만료된 토큰입니다.";

    public ExpiredTokenException() {
        super(ERROR_MSG, HttpStatus.UNAUTHORIZED);
    }
}
