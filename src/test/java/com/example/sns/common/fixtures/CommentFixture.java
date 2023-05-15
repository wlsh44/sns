package com.example.sns.common.fixtures;

import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.domain.Comment;
import com.example.sns.member.domain.Member;
import com.example.sns.post.domain.Post;

public class CommentFixture {

    public static final String BASIC_COMMENT_CONTENT1 = "content1";
    public static final String BASIC_COMMENT_CONTENT2 = "content2";

    public static NewCommentRequest getBasicCommentRequest(Long postId) {
        return new NewCommentRequest(postId, BASIC_COMMENT_CONTENT1);
    }

    public static NewCommentRequest getEmptyContentCommentRequest(Long feedId) {
        return new NewCommentRequest(feedId, "");
    }

    public static Comment getBasicComment1(Member member, Post post) {
        Comment comment = Comment.createComment(member, BASIC_COMMENT_CONTENT1);
        post.addComment(comment);
        return comment;
    }

    public static Comment getBasicComment2(Member member, Post post) {
        Comment comment = Comment.createComment(member, BASIC_COMMENT_CONTENT2);
        post.addComment(comment);
        return comment;
    }
}
