package com.example.sns.common.exception;

import com.example.sns.auth.exception.AuthException;
import com.example.sns.common.exception.dto.ErrorResponse;
import com.example.sns.feed.exception.CommentException;
import com.example.sns.feed.exception.FeedException;
import com.example.sns.imagestore.exception.ImageStoreException;
import com.example.sns.member.exception.MemberException;
import com.example.sns.social.exception.SocialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SnsControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> authException(AuthException e) {
        log.error("인증 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> memberException(MemberException e) {
        log.error("유저 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(SocialException.class)
    public ResponseEntity<ErrorResponse> socialException(SocialException e) {
        log.error("소셜 기능 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(ImageStoreException.class)
    public ResponseEntity<ErrorResponse> imageException(ImageStoreException e) {
        log.error("이미지 저장 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(FeedException.class)
    public ResponseEntity<ErrorResponse> feedException(FeedException e) {
        log.error("피드 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> comment(CommentException e) {
        log.error("댓글 에러 = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<ErrorResponse> snsException(SnsException e) {
        log.error("e.getErrorMsg() = {}", e.getErrorMsg());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorMsg()));
    }
}
