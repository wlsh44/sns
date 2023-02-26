package com.example.sns.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthExtractException extends AuthException {

    public static final String ERROR_MSG = "올바르지 않은 인증 헤더입니다.";

    public AuthExtractException() {
        super(ERROR_MSG, HttpStatus.UNAUTHORIZED);
    }
}
