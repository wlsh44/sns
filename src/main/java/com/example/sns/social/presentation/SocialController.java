package com.example.sns.social.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.social.application.SocialService;
import com.example.sns.social.application.dto.FollowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@Authenticated Long memberId, @RequestBody FollowRequest request) {
        socialService.follow(memberId, request);
        return ResponseEntity.ok().build();
    }
}
