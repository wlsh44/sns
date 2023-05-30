package com.example.sns.post.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            "join Follow f on f.following.id = p.author.member.id " +
            "where f.follower.id = :memberId order by p.createdAt desc")
    Slice<Post> findMyFeed(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Post p order by p.createdAt desc")
    Slice<Post> findRecentFeed(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.likeCount = p.likeCount + 1 where p.id = :postId")
    void increaseLikeCount(@Param("postId") Long postId);
}
