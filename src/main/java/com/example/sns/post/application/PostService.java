package com.example.sns.post.application;

import com.example.sns.post.application.dto.PostUpdateRequest;
import com.example.sns.post.application.dto.PostUploadRequest;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
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
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ImageStore imageStore;
    private final CommentRepository commentRepository;

    @Transactional
    public void uploadPost(Long memberId, PostUploadRequest request, List<MultipartFile> images) {
        Member member = getMember(memberId);
        Post post = savePost(request, member);

        List<PostImage> postImages = savePostImages(images, post);

        post.updateFeedImage(postImages);
    }

    private Post savePost(PostUploadRequest request, Member member) {
        Post newPost = Post.createFeed(member, request.getContent());
        return postRepository.save(newPost);
    }

    private List<PostImage> savePostImages(List<MultipartFile> images, Post post) {
        List<String> imagePaths = imageStore.storeFeedImages(images);
        return imagePaths.stream()
                .map(imagePath -> new PostImage(imagePath, post))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePost(Long memberId, Long feedId, PostUpdateRequest request, List<MultipartFile> images) {
        Post post = postRepository.findByIdAndMemberId(feedId, memberId)
                .orElseThrow(() -> new PostNotFoundException(feedId));

        List<PostImage> postImages = savePostImages(images, post);
        post.editFeed(request.getContent(), postImages);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional
    public void deletePost(Long memberId, Long feedId) {
        Post post = postRepository.findByIdAndMemberId(feedId, memberId)
                .orElseThrow(() -> new PostNotFoundException(feedId));
        commentRepository.deleteAllInBatch(post.getComments());
        post.deleteFeed();
        postRepository.delete(post);
    }
}
