package com.example.sns.user.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Embedded
    private UserInfo info;

    private String profileUrl;

    private String biography;

    private Member(String socialId, String userName, String nickName, String email) {
        this.socialId = socialId;
        this.info = new UserInfo(userName, nickName, email);
    }

    public static Member createUserFrom(OAuthUserInfoDto userInfo) {
        String nickName = getNickNameFromEmail(userInfo);
        return new Member(userInfo.getSocialId(), userInfo.getName(), nickName, userInfo.getEmail());
    }

    private static String getNickNameFromEmail(OAuthUserInfoDto userInfo) {
        return userInfo.getEmail().split("@")[0];
    }
}
