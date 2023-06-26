package com.example.sns.like.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository {
    void save(Like like);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
    void removeByMemberIdAndPostId(Long memberId, Long postId);
    int countByPostId(Long postId);
}
