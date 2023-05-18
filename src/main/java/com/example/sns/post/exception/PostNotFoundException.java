package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends SnsException {

    public static final String ERROR_MSG = "게시물이 존재하지 않습니다. id = %d";

    public PostNotFoundException(Long feedId) {
        super(String.format(ERROR_MSG, feedId), HttpStatus.BAD_REQUEST);
    }
}
