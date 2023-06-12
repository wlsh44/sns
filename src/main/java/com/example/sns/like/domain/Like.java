package com.example.sns.like.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.member.domain.Member;
import com.example.sns.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "memberIdPostId",
                        columnNames= {"member_id", "post_id"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Like(Post post, Member member) {
        this.post = post;
        this.member = member;
    }

    public boolean hasMember(Member member) {
        return this.member.equals(member);
    }
}