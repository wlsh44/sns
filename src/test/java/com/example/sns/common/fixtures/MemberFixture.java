package com.example.sns.common.fixtures;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.member.domain.Member;

public class MemberFixture {

    public static final String BASIC_NAME = "name";
    public static final String BASIC_NICKNAME = "nickname";
    public static final String BASIC_EMAIL = "nickname@test.test";
    public static final String BASIC_SOCIAL_ID = "1234567890";
    public static final String BASIC_PROFILE = "profile url";

    public static Member getBasicMember() {
        return Member.createUserFrom(new OAuthUserInfoDto(BASIC_NAME, BASIC_EMAIL, BASIC_SOCIAL_ID));
    }

    public static final String BASIC_NAME2 = "name2";
    public static final String BASIC_NICKNAME2 = "nickname2";
    public static final String BASIC_EMAIL2 = "nickname2@test.test";
    public static final String BASIC_SOCIAL_ID2 = "1234567891";

    public static Member getBasicMember2() {
        return Member.createUserFrom(new OAuthUserInfoDto(BASIC_NAME2, BASIC_EMAIL2, BASIC_SOCIAL_ID2));
    }

    public static final String FOLLOWER_NAME = "follower";
    public static final String FOLLOWER_NICKNAME = "follower";
    public static final String FOLLOWER_EMAIL = "follower@test.test";
    public static final String FOLLOWER_SOCIAL_ID = "1234567891";

    public static Member getFollower() {
        return Member.createUserFrom(new OAuthUserInfoDto(FOLLOWER_NAME, FOLLOWER_EMAIL, FOLLOWER_SOCIAL_ID));
    }

    public static final String FOLLOWING_NAME = "following";
    public static final String FOLLOWING_NICKNAME = "following";
    public static final String FOLLOWING_EMAIL = "following@test.test";
    public static final String FOLLOWING_SOCIAL_ID = "1234567892";

    public static Member getFollowing() {
        return Member.createUserFrom(new OAuthUserInfoDto(FOLLOWING_NAME, FOLLOWING_EMAIL, FOLLOWING_SOCIAL_ID));
    }
}
