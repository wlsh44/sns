package com.example.sns.common.fixtures;

import com.example.sns.social.application.dto.FollowRequest;
import com.example.sns.social.application.dto.UnfollowRequest;

public class SocialFixture {

    public static final Long followId = 2L;
    public static final Long unfollowId = 2L;

    public static FollowRequest getFollowRequest() {
        return new FollowRequest(followId);
    }

    public static UnfollowRequest getUnfollowRequest() {
        return new UnfollowRequest(unfollowId);
    }
}
