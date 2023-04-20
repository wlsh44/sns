package com.example.sns.common.imagestore.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class TemporaryFileException extends SnsException {

    public static final String TRANSFER_ERROR = "MultipartFile에서 File로 데이터 전송에 실패했습니다.";
    public static final String CREATE_ERROR = "임시 File 생성에 실패했습니다.";

    public TemporaryFileException(String errorMsg) {
        super(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
