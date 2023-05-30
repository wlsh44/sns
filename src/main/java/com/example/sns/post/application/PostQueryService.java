package com.example.sns.post.application;

import com.example.sns.like.domain.LikeRepository;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostResponse findPost(Long memberId, Long postId) {
        Post post = getPost(postId);

        boolean like = likeRepository.existsByMemberIdAndPostId(memberId, postId);
        return PostResponse.from(post, like);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }
}
