package com.example.sns.feed.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.feed.application.FeedService;
import com.example.sns.feed.application.dto.FeedUpdateRequest;
import com.example.sns.feed.application.dto.FeedUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping("")
    public ResponseEntity<Void> uploadFeed(@Authenticated Long memberId,
                                           @RequestPart(value = "dto") FeedUploadRequest request,
                                           @RequestPart List<MultipartFile> feedImages) {
        feedService.uploadFeed(memberId, request, feedImages);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{feedId}")
    public ResponseEntity<Void> updateFeed(@Authenticated Long memberId,
                                           @PathVariable Long feedId,
                                           @RequestPart(value = "dto") FeedUpdateRequest request,
                                           @RequestPart List<MultipartFile> feedImages) {
        feedService.updateFeed(memberId, feedId, request, feedImages);
        return ResponseEntity.ok().build();
    }
}
