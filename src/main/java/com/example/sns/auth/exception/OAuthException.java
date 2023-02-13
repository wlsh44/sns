package com.example.sns.auth.exception;

import org.springframework.http.HttpStatus;

public class OAuthException extends AuthException {

    private static final String ERROR_MSG = "OAuth 인증에 실패했습니다.";

    public OAuthException() {
        super(ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
