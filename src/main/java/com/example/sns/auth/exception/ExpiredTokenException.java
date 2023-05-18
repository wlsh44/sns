package com.example.sns.auth.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends SnsException {

    public static final String ERROR_MSG = "만료된 토큰입니다.";

    public ExpiredTokenException() {
        super(ERROR_MSG, HttpStatus.UNAUTHORIZED);
    }
}
