package com.example.sns.auth.presentation;

import com.example.sns.auth.exception.AuthExtractException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthExtractor {

    private static final String TOKEN_TYPE = "Bearer";
    private static final String BEARER_DELIMITER = " ";
    private static final int TOKEN_TYPE_INDEX = 0;
    private static final int TOKEN_LENGTH = 2;

    public String extractAuthToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        return getToken(authHeader);
    }

    private String getToken(String authHeader) {
        validateAuthHeaderExists(authHeader);
        String[] authContents = authHeader.split(BEARER_DELIMITER);
        validateStartWithTokenType(authContents);
        return authContents[1];
    }

    private void validateAuthHeaderExists(String authHeader) {
        if (Strings.isEmpty(authHeader)) {
            throw new AuthExtractException();
        }
    }

    private void validateStartWithTokenType(String[] authContents) {
        if (!(authContents.length == TOKEN_LENGTH && TOKEN_TYPE.equals(authContents[TOKEN_TYPE_INDEX]))) {
            throw new AuthExtractException();
        }
    }
}
