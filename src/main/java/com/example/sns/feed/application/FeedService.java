package com.example.sns.feed.application;

import com.example.sns.feed.application.dto.FeedUpdateRequest;
import com.example.sns.feed.application.dto.FeedUploadRequest;
import com.example.sns.feed.domain.Feed;
import com.example.sns.feed.domain.FeedImage;
import com.example.sns.feed.domain.FeedImageRepository;
import com.example.sns.feed.domain.FeedRepository;
import com.example.sns.feed.exception.FeedNotFoundException;
import com.example.sns.imagestore.infrastructure.ImageStore;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;
    private final ImageStore imageStore;

    @Transactional
    public void uploadFeed(Long memberId, FeedUploadRequest request, List<MultipartFile> images) {
        Member member = getMember(memberId);
        Feed feed = saveFeed(request, member);

        List<FeedImage> feedImages = saveFeedImages(images, feed);

        feed.updateFeedImage(feedImages);
    }

    private Feed saveFeed(FeedUploadRequest request, Member member) {
        Feed newFeed = Feed.createFeed(member, request.getContent());
        return feedRepository.save(newFeed);
    }

    private List<FeedImage> saveFeedImages(List<MultipartFile> images, Feed feed) {
        List<String> imagePaths = imageStore.storeFeedImages(images);
        return imagePaths.stream()
                .map(imagePath -> new FeedImage(imagePath, feed))
                .map(feedImageRepository::save)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateFeed(Long memberId, Long feedId, FeedUpdateRequest request, List<MultipartFile> images) {
        Feed feed = feedRepository.findByIdAndMemberId(feedId, memberId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));

        List<FeedImage> feedImages = saveFeedImages(images, feed);
        feed.editFeed(request.getContent(), feedImages);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional
    public void deleteFeed(Long memberId, Long feedId) {
        Feed feed = feedRepository.findByIdAndMemberId(feedId, memberId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));
        feedRepository.delete(feed);
    }
}
