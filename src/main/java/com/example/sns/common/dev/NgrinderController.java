package com.example.sns.common.dev;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.auth.application.JwtProvider;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@Profile("dev")
@RestController
@RequestMapping("ngrinder")
@RequiredArgsConstructor
public class NgrinderController {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final Random random = new Random();

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResponse> signUp() {
        int randomNumber = random.nextInt();
        String name = "test" + randomNumber;
        String email = "email" + randomNumber + "@test.com";
        Member member = memberRepository.save(Member.createUserFrom(new OAuthUserInfoDto(name, email, String.valueOf(randomNumber))));

        String token = jwtProvider.createToken(member.getId());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
