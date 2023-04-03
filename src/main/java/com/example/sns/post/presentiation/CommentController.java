package com.example.sns.post.presentiation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.post.application.CommentService;
import com.example.sns.post.application.dto.NewCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Void> create(@Authenticated Long memberId, @RequestBody NewCommentRequest request) {
        commentService.createComment(memberId, request);
        return ResponseEntity.ok().build();
    }
}
