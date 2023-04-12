package com.example.sns.follow.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.follow.application.FollowService;
import com.example.sns.follow.application.dto.FollowRequest;
import com.example.sns.follow.application.dto.UnfollowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@Authenticated Long memberId, @RequestBody FollowRequest request) {
        followService.follow(memberId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@Authenticated Long memberId, @RequestBody UnfollowRequest request) {
        followService.unfollow(memberId, request);
        return ResponseEntity.ok().build();
    }
}
