package com.example.sns.post.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.post.application.PostService;
import com.example.sns.post.application.dto.PostUpdateRequest;
import com.example.sns.post.application.dto.PostUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Void> uploadPost(@Authenticated Long memberId,
                                           @RequestPart(value = "dto") PostUploadRequest request,
                                           @RequestPart List<MultipartFile> feedImages) {
        postService.uploadPost(memberId, request, feedImages);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{feedId}")
    public ResponseEntity<Void> updatePost(@Authenticated Long memberId,
                                           @PathVariable Long feedId,
                                           @RequestPart(value = "dto") PostUpdateRequest request,
                                           @RequestPart List<MultipartFile> feedImages) {
        postService.updatePost(memberId, feedId, request, feedImages);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deletePost(@Authenticated Long memberId, @PathVariable Long feedId) {
        postService.deletePost(memberId, feedId);
        return ResponseEntity.ok().build();
    }
}
