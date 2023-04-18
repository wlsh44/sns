package com.example.sns.post.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.post.application.CommentService;
import com.example.sns.post.application.dto.NewCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> create(@Authenticated Long memberId, @PathVariable Long postId, NewCommentRequest request) {
        commentService.createComment(memberId, postId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> delete(@Authenticated Long memberId, @PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }
}
