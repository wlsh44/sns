package com.example.sns.member.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Embedded
    private DetailedInfo detailedInfo;

    @Embedded
    private SocialInfo socialInfo;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private final List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Device> devices = new ArrayList<>();

    private Member(String socialId, String name, String username, String email) {
        this.socialId = socialId;
        this.detailedInfo = new DetailedInfo(name, email);
        this.socialInfo = new SocialInfo(username);
    }

    public static Member createUserFrom(OAuthUserInfoDto userInfo) {
        String username = getUsernameFromEmail(userInfo);
        return new Member(userInfo.getSocialId(), userInfo.getName(), username, userInfo.getEmail());
    }

    private static String getUsernameFromEmail(OAuthUserInfoDto userInfo) {
        return userInfo.getEmail().split("@")[0];
    }

    public void follow(Member following) {
        validateAlreadyFollow(following);
        Follow followTable = Follow.createFollowTable(this, following);
        followings.add(followTable);
        following.getFollowers().add(followTable);
    }

    public boolean isFollower(Member member) {
        return followers.stream()
                .anyMatch(follow -> follow.isFollowing(member, this));
    }

    public boolean isFollowing(Member member) {
        return followings.stream()
                .anyMatch(follow -> follow.isFollowing(this, member));
    }

    private void validateAlreadyFollow(Member following) {
        if (isFollowing(following)) {
            throw new AlreadyFollowException();
        }
    }

    public void unfollow(Member following) {
        followings.remove(getFollow(following));
    }

    private Follow getFollow(Member following) {
        return followings.stream()
                .filter(follow -> follow.isFollowing(this, following))
                .findAny()
                .orElseThrow(NotFollowingMemberException::new);
    }

    public void update(String username, String biography, String profilePath) {
        this.socialInfo.update(username, biography, profilePath);
    }

    public List<String> getDeviceTokens() {
        return devices.stream()
                .map(Device::getToken)
                .toList();
    }

    public void addDevice(String token) {
        this.devices.add(new Device(token, this));
    }
}
