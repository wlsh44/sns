package com.example.sns.auth.infrastructure;

import com.example.sns.auth.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GoogleClient {

    private static final String SCOPE_DELIMITER = " ";
    private static final String RESPONSE_TYPE = "code";

    private final AuthProperties properties;

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
}
