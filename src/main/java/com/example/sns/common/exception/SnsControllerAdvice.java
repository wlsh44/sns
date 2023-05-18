package com.example.sns.common.exception;

import com.example.sns.auth.exception.ExpiredTokenException;
import com.example.sns.common.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SnsControllerAdvice {

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponse> authException(ExpiredTokenException e) {
        log.info("인증 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> argumentNotValidException(MethodArgumentNotValidException e) {
        log.info("e = ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<ErrorResponse> snsException(SnsException e) {
        HttpStatus status = e.getStatus();
        if (status.is4xxClientError()) {
            log.info("클라이언트 예외 = {}", e.getErrorMsg());
        } else if (status.is5xxServerError()) {
            log.error("서버 예외.", e);
        }
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("예상하지 못한 예외", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse());
    }
}
