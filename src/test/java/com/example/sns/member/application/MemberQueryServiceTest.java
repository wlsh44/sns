package com.example.sns.member.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.application.FollowService;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import com.example.sns.member.presentation.dto.MemberSearchDto;
import com.example.sns.member.presentation.dto.MemberSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberQueryServiceTest extends ServiceTest {

    @Autowired
    MemberQueryService memberQueryService;

    @Autowired
    FollowService followService;

    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest() throws Exception {
        //given
        Member loginMember = memberRepository.save(getBasicMember());
        Member profileMember = memberRepository.save(getBasicMember2());
        followService.follow(loginMember.getId(), profileMember.getId());
        MemberProfileResponse expect = getMemberProfileExpect(profileMember);

        //when
        MemberProfileResponse response = memberQueryService.getProfile(loginMember.getId(), profileMember.getSocialInfo().getUsername());

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private MemberProfileResponse getMemberProfileExpect(Member member) {
        return new MemberProfileResponse(
                member.getId(),
                member.getDetailedInfo().getName(),
                member.getSocialInfo().getUsername(),
                member.getSocialInfo().getProfileUrl(),
                member.getSocialInfo().getBiography(),
                1,
                0,
                true
        );
    }


    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest_loginMemberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member profileMember = memberRepository.save(getBasicMember2());

        //when
        assertThatThrownBy(() -> memberQueryService.getProfile(notExistId, profileMember.getSocialInfo().getUsername()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest_profileMemberNotFound() throws Exception {
        //given
        String notExistName = "notExistName";
        Member loginMember = memberRepository.save(getBasicMember2());

        //when
        assertThatThrownBy(() -> memberQueryService.getProfile(loginMember.getId(), notExistName))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("멤버 정보를 가져와야 함")
    void getMemberInfoTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        MemberInfoResponse expect = getMemberInfoExpect(member);

        //when
        MemberInfoResponse response = memberQueryService.getMemberInfo(member.getId());

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private MemberInfoResponse getMemberInfoExpect(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getDetailedInfo().getName(),
                member.getSocialInfo().getUsername(),
                member.getSocialInfo().getProfileUrl(),
                member.getSocialInfo().getBiography(),
                member.getDetailedInfo().getEmail()
        );
    }

    @Test
    @DisplayName("없는 멤버 정보를 가져오려고 할 경우 예외가 발생해야 함")
    void getMemberInfoTest_memberNotFound() throws Exception {
        //given
        Long notExistId = 999L;

        //when then
        assertThatThrownBy(() -> memberQueryService.getMemberInfo(notExistId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("유저 닉네임을 포함하는 유저들의 정보를 가져와야 함")
    void searchMembersTest() throws Exception {
        //given
        String search = "name";
        Member member1 = memberRepository.save(getBasicMember());
        Member member2 = memberRepository.save(getBasicMember2());
        Pageable pageable = PageRequest.of(0, 10);
        MemberSearchResponse expect = getSearchMembersExpect(member1, member2);

        //when
        MemberSearchResponse response = memberQueryService.searchMembers(search, pageable);

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);

    }

    private MemberSearchResponse getSearchMembersExpect(Member member1, Member member2) {
        return new MemberSearchResponse(
                List.of(
                        new MemberSearchDto(member1.getId(), member1.getSocialInfo().getUsername(), member1.getSocialInfo().getProfileUrl()),
                        new MemberSearchDto(member2.getId(), member2.getSocialInfo().getUsername(), member2.getSocialInfo().getProfileUrl())
                ),
                true,
                0
        );
    }
}