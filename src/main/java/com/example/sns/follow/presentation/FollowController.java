package com.example.sns.follow.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.follow.application.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/members/{followingId}/follow")
    public ResponseEntity<Void> follow(@Authenticated Long memberId, @PathVariable Long followingId) {
        followService.follow(memberId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{followingId}/follow")
    public ResponseEntity<Void> unfollow(@Authenticated Long memberId, @PathVariable Long followingId) {
        followService.unfollow(memberId, followingId);
        return ResponseEntity.ok().build();
    }
}
