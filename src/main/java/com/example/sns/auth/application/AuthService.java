package com.example.sns.auth.application;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.auth.infrastructure.GoogleClient;
import com.example.sns.auth.infrastructure.JwtProvider;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.member.domain.Member;
import com.example.sns.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final GoogleClient client;
    private final JwtProvider jwtProvider;

    public URI getAuthRedirectURI() {
        return client.getAuthRedirectURI();
    }

    public TokenResponse signIn(String code) {
        OAuthUserInfoDto userInfo = getUserInfo(code);
        Member member = memberRepository.findBySocialId(userInfo.getSocialId())
                .orElseGet(() -> signUp(userInfo));
        String token = jwtProvider.createToken(member.getId());

        return new TokenResponse(token);
    }

    private OAuthUserInfoDto getUserInfo(String code) {
        String idToken = client.getIdToken(code);
        return client.getUserInfo(idToken);
    }

    private Member signUp(OAuthUserInfoDto userInfo) {
        Member member = Member.createUserFrom(userInfo);
        return memberRepository.save(member);
    }
}
