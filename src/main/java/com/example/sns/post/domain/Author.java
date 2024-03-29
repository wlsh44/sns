package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return member.getId();
    }

    public String getProfile() {
        return member.getSocialInfo().getProfileUrl();
    }

    public String getUsername() {
        return member.getSocialInfo().getUsername();
    }

    boolean isAuthor(Long memberId) {
        return member.getId().equals(memberId);
    }
}
