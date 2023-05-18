package com.example.sns.post.application;

import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.presentiation.dto.CommentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public CommentsResponse findComments(Long postId, Pageable pageable) {
        Slice<Comment> commentSlice = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);

        return CommentsResponse.from(commentSlice.getContent(), commentSlice.hasNext(), commentSlice.getNumber());
    }
}
