package com.example.sns.auth.exception;

import com.example.sns.common.exception.SnsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends SnsException {
    public static final String ERROR_MSG = "예상하지 못한 인증 에러입니다.";

    public AuthException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }

    public AuthException() {
        super(ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
