package com.example.sns.auth.presentation;

import com.example.sns.auth.application.AuthService;
import com.example.sns.auth.presentation.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectToProvider() {
        URI redirectURI = authService.getAuthRedirectURI();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectURI);

        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .headers(headers)
                .build();
    }

    @GetMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestParam String code) {
        return ResponseEntity.ok(authService.signIn(code));
    }
}
