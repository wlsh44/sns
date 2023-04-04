package com.example.sns.common.fixtures;

import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.Post;
import com.example.sns.member.domain.Member;

public class CommentFixture {

    public static final String BASIC_COMMENT_CONTENT = "content";

    public static NewCommentRequest getBasicCommentRequest(Long feedId) {
        return new NewCommentRequest(feedId, BASIC_COMMENT_CONTENT);
    }

    public static NewCommentRequest getEmptyContentCommentRequest(Long feedId) {
        return new NewCommentRequest(feedId, "");
    }

    public static Comment getBasicComment(Member member) {
        return Comment.createComment(member, BASIC_COMMENT_CONTENT);
    }
}
