package com.example.sns.auth.application;

import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class AuthService {

    public URI getRedirectURI() {
    public URI getAuthRedirectURI() {
        return client.getAuthRedirectURI();
    }
        return null;
    }
}
