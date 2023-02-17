package com.example.sns.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {
    public static final String ERROR_MSG = "예상하지 못한 인증 에러입니다.";

    private final String errorMsg;
    private final HttpStatus status;

    public AuthException() {
        this.errorMsg = ERROR_MSG;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
