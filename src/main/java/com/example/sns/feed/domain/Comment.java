package com.example.sns.feed.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.feed.exception.EmptyCommentException;
import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private String content;

    public Comment(Member member, Feed feed, String content) {
        this.member = member;
        this.feed = feed;
        this.content = content;
    }

    public static Comment createComment(Member member, Feed feed, String content) {
        validateContent(content);
        return new Comment(member, feed, content);
    }

    private static void validateContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new EmptyCommentException();
        }
    }
}
