package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;

class LikeTest {

    @Test
    @DisplayName("좋아요를 누른 멤버일 경우 true를 리턴해야 함")
    void hasMemberTest() throws Exception {
        //given
        Member member = getBasicMember();
        Post post = getBasicPost(member);
        Like like = new Like(post, member);

        //when
        boolean res = like.hasMember(member);

        //then
        assertThat(res).isTrue();
    }
}