package com.example.sns.post.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.post.exception.EmptyCommentException;
import com.example.sns.member.domain.Member;
import com.example.sns.post.exception.NotCommentAuthorException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @Embedded
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    public Comment(Author author, String content) {
        validateContent(content);
        this.author = author;
        this.content = content;
    }

    public static Comment createComment(Member member, String content) {
        return new Comment(new Author(member), content);
    }

    private void validateContent(String content) {
        String strippedContent = content.strip();
        if (!StringUtils.hasText(strippedContent)) {
            throw new EmptyCommentException();
        }
    }

    public void mappingPost(Post post) {
        this.post = post;
    }

    public void validateIsAuthor(Long memberId) {
        if (!author.isAuthor(memberId)) {
            throw new NotCommentAuthorException();
        }
    }
}
