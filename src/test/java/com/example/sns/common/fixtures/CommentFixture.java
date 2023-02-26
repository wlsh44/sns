package com.example.sns.common.fixtures;

import com.example.sns.feed.application.dto.NewCommentRequest;
import com.example.sns.feed.domain.Comment;
import com.example.sns.feed.domain.Feed;
import com.example.sns.member.domain.Member;

public class CommentFixture {

    public static final String BASIC_COMMENT_CONTENT = "content";

    public static NewCommentRequest getBasicCommentRequest(Long feedId) {
        return new NewCommentRequest(feedId, BASIC_COMMENT_CONTENT);
    }

    public static NewCommentRequest getEmptyContentCommentRequest(Long feedId) {
        return new NewCommentRequest(feedId, "");
    }

    public static Comment getBasicComment(Member member, Feed feed) {
        return Comment.createComment(member, feed, BASIC_COMMENT_CONTENT);
    }
}
