package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlreadyLikedPostException extends SnsException {

    public static final String ERROR_MSG = "이미 좋아요를 누른 게시글입니다.";

    public AlreadyLikedPostException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
