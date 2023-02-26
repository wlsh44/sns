package com.example.sns.common.controller;

import com.example.sns.auth.application.AuthService;
import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.infrastructure.JwtProvider;
import com.example.sns.auth.presentation.AuthExtractor;
import com.example.sns.feed.application.CommentService;
import com.example.sns.feed.application.FeedService;
import com.example.sns.social.application.SocialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    protected AuthExtractor authExtractor;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected FeedService feedService;

    @MockBean
    protected SocialService socialService;

    @MockBean
    protected CommentService commentService;
}
