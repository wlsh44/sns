package com.example.sns.alarm.application;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.alarm.domain.AlarmRepository;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.application.FollowService;
import com.example.sns.follow.application.FollowedEvent;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.post.application.PostCommandService;
import com.example.sns.post.application.PostImageStore;
import com.example.sns.post.application.PostUploadedEvent;
import com.example.sns.post.domain.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.sns.alarm.domain.AlarmType.POST_UPLOAD;
import static com.example.sns.common.fixtures.MemberFixture.getFollower;
import static com.example.sns.common.fixtures.MemberFixture.getFollowing;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH1;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPostImages;
import static com.example.sns.common.fixtures.PostFixture.getBasicUploadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RecordApplicationEvents
class AlarmEventHandlerTest extends ServiceTest {

    @Autowired
    AlarmEventHandler alarmEventHandler;

    @MockBean
    AlarmService alarmService;

    @Autowired
    AlarmRepository alarmRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowService followService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommandService postCommandService;

    @MockBean
    PostImageStore imageStore;

    @Autowired
    ApplicationEvents events;

    @Autowired
    @Qualifier("asyncThreadPoolTaskExecutor")
    ThreadPoolTaskExecutor executor;

    @Test
    @DisplayName("팔로우를 하면 팔로우 알람 이벤트가 발생해야 함")
    void sendFollowedAlarmEventTest() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());

        //when
        followService.follow(follower.getId(), following.getId());
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        int count = (int) events.stream(FollowedEvent.class).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("팔로우 알람 이벤트가 발생하면 알람이 저장되어야 함")
    void sendFollowedAlarmEventTest_alarmSave() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());

        //when
        alarmEventHandler.sendFollowedAlarm(new FollowedEvent(follower, following));
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        List<Alarm> alarms = alarmRepository.findAll();
        assertThat(alarms).hasSize(1);
    }

    @Test
    @DisplayName("게시글을 작성하면 게시글 작성 알람 이벤트가 발생해야 함")
    void sendPostUploadedAlarmEventTest() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());
        List<String> imagePaths = List.of(POST_IMAGE_PATH1, POST_IMAGE_PATH2);
        given(imageStore.savePostImages(any()))
                .willReturn(imagePaths);

        //when
        postCommandService.uploadPost(following.getId(), getBasicUploadRequest(), getBasicPostImages());
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        int count = (int) events.stream(PostUploadedEvent.class).count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글을 작성하면 게시글 작성 알람이 저장되어야 함")
    void sendPostUploadedAlarmEventTest_alarmSave() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());
        followService.follow(follower.getId(), following.getId());

        //when
        postCommandService.uploadPost(following.getId(), getBasicUploadRequest(), getBasicPostImages());
        executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        //then
        List<Alarm> alarms = alarmRepository.findAll();
        assertThat(alarms).hasSize(1);
        assertThat(alarms.get(0).getTarget().getId()).isEqualTo(follower.getId());
        assertThat(alarms.get(0).getText()).isEqualTo(POST_UPLOAD.getText(follower.getInfo().getNickname()));
    }
}