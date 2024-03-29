package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends SnsException {

    public static final String ERROR_MSG = "댓글이 존재하지 않습니다. id = %d";

    public CommentNotFoundException(Long commentId) {
        super(String.format(ERROR_MSG, commentId), HttpStatus.BAD_REQUEST);
    }
}
