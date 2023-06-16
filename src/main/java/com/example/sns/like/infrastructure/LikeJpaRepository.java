package com.example.sns.like.infrastructure;

import com.example.sns.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Modifying
    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    int countByPostId(Long postId);
}
