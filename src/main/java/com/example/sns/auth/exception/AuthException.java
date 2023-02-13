package com.example.sns.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {
    private final String errorMsg;
    private final HttpStatus status;
}
