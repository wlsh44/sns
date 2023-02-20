package com.example.sns.member.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.social.domain.Follow;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Embedded
    private MemberInfo info;

    private String profileUrl;

    private String biography;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> followings = new ArrayList<>();

    private Member(String socialId, String userName, String nickName, String email) {
        this.socialId = socialId;
        this.info = new MemberInfo(userName, nickName, email);
    }

    public static Member createUserFrom(OAuthUserInfoDto userInfo) {
        String nickName = getNickNameFromEmail(userInfo);
        return new Member(userInfo.getSocialId(), userInfo.getName(), nickName, userInfo.getEmail());
    }

    private static String getNickNameFromEmail(OAuthUserInfoDto userInfo) {
        return userInfo.getEmail().split("@")[0];
    }

    public Follow follow(Member follower) {
        Follow followTable = Follow.createFollowTable(this, follower);
        followings.add(followTable);
        return followTable;
    }
}
