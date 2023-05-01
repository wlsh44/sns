package com.example.sns.common.infrastructure.imagestore.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class ImageStoreException extends SnsException {

    public static final String ERROR_MSG = "이미지 저장에 실패하였습니다.";

    public ImageStoreException() {
        super(ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ImageStoreException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }
}
