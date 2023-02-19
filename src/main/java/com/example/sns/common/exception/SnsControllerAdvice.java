package com.example.sns.common.exception;

import com.example.sns.auth.exception.AuthException;
import com.example.sns.common.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SnsControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> authException(AuthException e) {
        log.error("e.getErrorMsg() = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<ErrorResponse> snsException(SnsException e) {
        log.error("e.getErrorMsg() = {}", e.getErrorMsg());
        log.error("e.getThrowable().getMessage() = {}", e.getThrowable().getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }
}
