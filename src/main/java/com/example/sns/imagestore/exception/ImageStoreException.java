package com.example.sns.imagestore.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class ImageStoreException extends SnsException {

    public static final String ERROR_MSG = "이미지 저장에 실패하였습니다.";

    public ImageStoreException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }

    public ImageStoreException(Throwable throwable) {
        super(ERROR_MSG, throwable);
    }
}
