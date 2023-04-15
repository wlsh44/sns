package com.example.sns.post.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.post.application.FeedService;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import com.example.sns.post.presentiation.dto.RecentFeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/me")
    public ResponseEntity<MyFeedResponse> findMyFeed(@Authenticated Long memberId, @PageableDefault(size = 5) Pageable pageable) {
        MyFeedResponse response = feedService.findMyFeed(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/timeline")
    public ResponseEntity<RecentFeedResponse> findRecentFeed(Pageable pageable) {
        RecentFeedResponse response = feedService.findRecentFeed(pageable);
        return ResponseEntity.ok(response);
    }
}
