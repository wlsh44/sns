package com.example.sns.post.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            "join Follow f on f.following.id = p.author.id " +
            "where f.follower.id = :memberId")
    List<Post> findMyFeed(@Param("memberId") Long memberId, Pageable pageable);
}
