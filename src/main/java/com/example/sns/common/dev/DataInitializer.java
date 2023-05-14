package com.example.sns.common.dev;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.post.domain.Author;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.example.sns.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final int dataSize = 1;

    @PostConstruct
    public void initData() {
        List<Member> members = initMembers();
        initPosts(members);
    }

    private void initPosts(List<Member> members) {
        for (int i = 0; i < dataSize; i++) {
            String content = "content" + i;
            Member member = members.get(i);
            Post post = new Post(new Author(member), content);
            post.addPostImage(new PostImage("http://dummyimage.com/213x100.png/dddddd/000000", post));
            postRepository.save(post);
        }
        log.info("POST {}개 초기화 완료", dataSize);
    }

    private List<Member> initMembers() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            String name = "member" + i;
            String email = String.format("email%d@test.com", i);
            String socialId = String.valueOf(i);
            Member member = memberRepository.save(Member.createUserFrom(new OAuthUserInfoDto(name, email, socialId)));
            members.add(member);
        }
        log.info("MEMBER {}개 초기화 완료", dataSize);
        return members;
    }
}
