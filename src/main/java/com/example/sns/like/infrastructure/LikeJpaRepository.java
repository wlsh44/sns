package com.example.sns.like.infrastructure;

import com.example.sns.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
}
