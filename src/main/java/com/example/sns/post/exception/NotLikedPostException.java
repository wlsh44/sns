package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class NotLikedPostException extends SnsException {

    public static final String ERROR_MSG = "좋아요를 누른 적이 없는 게시글입니다.";

    public NotLikedPostException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
