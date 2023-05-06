package com.example.sns.post.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.post.application.CommentCommandService;
import com.example.sns.post.application.CommentQueryService;
import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.presentiation.dto.CommentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> create(@Authenticated Long memberId, @PathVariable Long postId, @RequestBody @Valid NewCommentRequest request) {
        commentCommandService.createComment(memberId, postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> delete(@Authenticated Long memberId, @PathVariable Long postId, @PathVariable Long commentId) {
        commentCommandService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<CommentsResponse> findComments(@PathVariable Long postId, @PageableDefault(size = 20) Pageable pageable) {
        CommentsResponse response = commentQueryService.findComments(postId, pageable);
        return ResponseEntity.ok(response);
    }
}
