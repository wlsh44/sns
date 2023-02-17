package com.example.sns.auth.presentation;

import com.example.sns.auth.infrastructure.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthExtractor authExtractor;
    private final JwtProvider jwtProvider;
    public static final String MEMBER_ID_KEY = "memberId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authExtractor.extractAuthToken(request);
        Long memberId = jwtProvider.parseMemberId(token);
        request.setAttribute(MEMBER_ID_KEY, memberId);
        return true;
    }
}
