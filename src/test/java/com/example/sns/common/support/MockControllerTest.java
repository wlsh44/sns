package com.example.sns.common.support;

import com.example.sns.common.infrastructure.fcm.AlarmService;
import com.example.sns.auth.application.AuthService;
import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.infrastructure.JwtProvider;
import com.example.sns.auth.presentation.AuthExtractor;
import com.example.sns.member.application.MemberCommandService;
import com.example.sns.member.application.MemberQueryService;
import com.example.sns.post.application.CommentCommandService;
import com.example.sns.post.application.CommentQueryService;
import com.example.sns.post.application.FeedService;
import com.example.sns.post.application.LikeService;
import com.example.sns.post.application.PostQueryService;
import com.example.sns.post.application.PostCommandService;
import com.example.sns.follow.application.FollowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
public class MockControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    protected AuthProperties authProperties;

    @MockBean
    protected JwtProvider jwtProvider;

    @SpyBean
    protected AuthExtractor authExtractor;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected PostCommandService postCommandService;

    @MockBean
    protected PostQueryService postQueryService;

    @MockBean
    protected FollowService followService;

    @MockBean
    protected CommentCommandService commentCommandService;

    @MockBean
    protected CommentQueryService commentQueryService;

    @MockBean
    protected MemberQueryService memberQueryService;

    @MockBean
    protected MemberCommandService memberCommandService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected FeedService feedService;

    @MockBean
    protected AlarmService alarmService;

    @MockBean
    protected FirebaseMessaging firebaseMessaging;
}
