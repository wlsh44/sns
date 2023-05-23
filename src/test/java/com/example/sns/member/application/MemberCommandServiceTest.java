package com.example.sns.member.application;

import com.example.sns.common.infrastructure.imagestore.ImageStore;
import com.example.sns.common.infrastructure.imagestore.exception.ImageStoreException;
import com.example.sns.common.infrastructure.imagestore.exception.InvalidImageException;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.application.dto.MemberUpdateRequest;
import com.example.sns.member.domain.Device;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.AlreadyExistUsernameException;
import com.example.sns.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_BIOGRAPHY2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_USERNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_USERNAME2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE2;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MemberCommandServiceTest extends ServiceTest {

    @Autowired
    MemberCommandService memberCommandService;

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    ImageStore imageStore;

    @Test
    @DisplayName("멤버 정보를 수정해야 함")
    void updateMemberTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        MemberUpdateRequest request = new MemberUpdateRequest(BASIC_USERNAME2, BASIC_BIOGRAPHY2);
        MultipartFile image = mock(MultipartFile.class);
        given(imageStore.saveImage(any(), any()))
                .willReturn(BASIC_PROFILE2);
        given(image.isEmpty())
                .willReturn(false);

        //when
        memberCommandService.updateMember(member.getId(), request, image);

        //then
        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertAll(
                () -> assertThat(updatedMember.getSocialInfo().getUsername()).isEqualTo(BASIC_USERNAME2),
                () -> assertThat(updatedMember.getSocialInfo().getBiography()).isEqualTo(BASIC_BIOGRAPHY2),
                () -> assertThat(updatedMember.getSocialInfo().getProfileUrl()).isEqualTo(BASIC_PROFILE2)
        );
    }

    @Test
    @DisplayName("없는 멤버의 정보를 수정하면 예외가 발생해야 함")
    void updateMemberTest_memberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        MemberUpdateRequest request = new MemberUpdateRequest(BASIC_USERNAME2, BASIC_BIOGRAPHY2);
        MultipartFile image = mock(MultipartFile.class);

        //when then
        assertThatThrownBy(() -> memberCommandService.updateMember(notExistId, request, image))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미 존재하는 닉네임으로 수정할 경우 예외가 발생해야 함")
    void updateMemberTest_alreadyExistUsername() throws Exception {
        //given
        memberRepository.save(getBasicMember());
        Long memberId = memberRepository.save(getBasicMember2()).getId();
        MemberUpdateRequest request = new MemberUpdateRequest(BASIC_USERNAME, BASIC_BIOGRAPHY2);
        MultipartFile image = mock(MultipartFile.class);

        //when then
        assertThatThrownBy(() -> memberCommandService.updateMember(memberId, request, image))
                .isInstanceOf(AlreadyExistUsernameException.class);
    }

    @Test
    @DisplayName("프로필 저장에 실패할 경우 예외가 발생해야 함")
    void updateMemberTest_imageStoreException() throws Exception {
        //given
        Long memberId = memberRepository.save(getBasicMember()).getId();
        MemberUpdateRequest request = new MemberUpdateRequest(BASIC_USERNAME2, BASIC_BIOGRAPHY2);
        MultipartFile image = mock(MultipartFile.class);
        given(imageStore.saveImage(any(), any()))
                .willThrow(ImageStoreException.class);
        given(image.isEmpty())
                .willReturn(false);

        //when then
        assertThatThrownBy(() -> memberCommandService.updateMember(memberId, request, image))
                .isInstanceOf(ImageStoreException.class);
    }

    @Test
    @DisplayName("아미지가 없을 경우 예외가 발생해야 함")
    void updateMemberTest_invalidImage() throws Exception {
        //given
        Long memberId = memberRepository.save(getBasicMember()).getId();
        MemberUpdateRequest request = new MemberUpdateRequest(BASIC_USERNAME2, BASIC_BIOGRAPHY2);
        MultipartFile image = mock(MultipartFile.class);
        given(image.isEmpty())
                .willReturn(true);

        //when then
        assertThatThrownBy(() -> memberCommandService.updateMember(memberId, request, image))
                .isInstanceOf(InvalidImageException.class);
    }

    @Test
    @DisplayName("디바이스 토큰을 추가해야 함")
    void addDeviceTokenTest() throws Exception {
        //given
        String token = "token";
        Member member = memberRepository.save(getBasicMember());

        //when
        memberCommandService.addDeviceToken(member.getId(), token);

        //then
        List<Device> devices = em.createQuery("select d from Device d", Device.class)
                .getResultList();
        assertThat(devices).hasSize(1);
        assertThat(devices.get(0).getToken()).isEqualTo(token);
    }
}