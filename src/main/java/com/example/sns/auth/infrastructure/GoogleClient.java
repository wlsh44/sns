package com.example.sns.auth.infrastructure;

import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.exception.AuthException;
import com.example.sns.auth.exception.OAuthException;
import com.example.sns.auth.presentation.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GoogleClient {

    private static final String SCOPE_DELIMITER = " ";
    private static final String RESPONSE_TYPE = "code";
    public static final String AUTHORIZATION_CODE = "authorization_code";

    private final AuthProperties properties;
    private final WebClient oauthProvider;

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
            System.out.println("e = " + e);
            throw new OAuthException();
        }
    }

    private MultiValueMap<String, String> getTokenUriFormData(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", properties.getClientId());
        formData.add("client_secret", properties.getClientSecret());
        formData.add("grant_type", AUTHORIZATION_CODE);
        formData.add("redirect_uri", properties.getRedirectUri());
        formData.add("code", code);
        return formData;
    }
}
