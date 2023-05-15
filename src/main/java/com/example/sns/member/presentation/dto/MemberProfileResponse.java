package com.example.sns.member.presentation.dto;

import com.example.sns.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberProfileResponse {

    private final Long id;
    private final String name;
    private final String username;
    private final String profileImage;
    private final String biography;
    private final int followerCnt;
    private final int followingCnt;

    @JsonProperty(value = "isFollowing")
    private final boolean following;

    @Builder
    public MemberProfileResponse(Long id, String name, String username, String profileImage, String biography, int followerCnt, int followingCnt, boolean following) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImage = profileImage;
        this.biography = biography;
        this.followerCnt = followerCnt;
        this.followingCnt = followingCnt;
        this.following = following;
    }

    public static MemberProfileResponse from(Member member, boolean isFollowing) {
        return new MemberProfileResponse(
                member.getId(),
                member.getInfo().getName(),
                member.getInfo().getUsername(),
                member.getProfileUrl(),
                member.getBiography(),
                member.getFollowers().size(),
                member.getFollowings().size(),
                isFollowing
        );
    }
}
