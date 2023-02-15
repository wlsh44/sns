package com.example.sns.auth.infrastructure;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.exception.OAuthException;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleClient {

    private static final int PAYLOAD_INDEX = 1;
    private static final String JWT_DELIMITER = "\\.";
    private static final String SCOPE_DELIMITER = " ";
    private static final String RESPONSE_TYPE = "code";

    private final AuthProperties properties;
    private final WebClient oauthProvider;
    private final ObjectMapper objectMapper;

    public URI getAuthRedirectURI() {
        MultiValueMap<String, String> parameters = getRedirectParameters();
        return UriComponentsBuilder
                .fromUriString(properties.getAuthUri())
                .queryParams(parameters)
                .build()
                .toUri();
    }

    private MultiValueMap<String, String> getRedirectParameters() {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", properties.getClientId());
        parameters.add("redirect_uri", properties.getRedirectUri());
        parameters.add("response_type", RESPONSE_TYPE);
        parameters.add("scope", String.join(SCOPE_DELIMITER, properties.getScopes()));
        return parameters;
    }

    public String getIdToken(String code) {
        try {
            TokenResponse tokenResponse = oauthProvider.post()
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(getTokenUriFormData(code))
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();

            return tokenResponse.getIdToken();
        } catch (Exception e) {
            throw new OAuthException();
        }
    }

    private MultiValueMap<String, String> getTokenUriFormData(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", properties.getClientId());
        formData.add("client_secret", properties.getClientSecret());
        formData.add("grant_type", properties.getGrantType());
        formData.add("redirect_uri", properties.getRedirectUri());
        formData.add("code", code);
        return formData;
    }

    public OAuthUserInfoDto getUserInfo(String idToken) {
        String payload = getPayload(idToken);
        String decode = decodeBase64Payload(payload);

        try {
            return objectMapper.readValue(decode, OAuthUserInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new OAuthException();
        }
    }

    private String getPayload(String idToken) {
        return idToken.split(JWT_DELIMITER)[PAYLOAD_INDEX];
    }

    private String decodeBase64Payload(String payload) {
        return new String(Base64Utils.decode(payload.getBytes()), StandardCharsets.UTF_8);
    }
}
