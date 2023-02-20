package com.example.sns.feed.domain;

import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feed")
    private List<FeedImage> images = new ArrayList<>();

    public Feed(Member member, String content) {
        this.content = content;
        this.member = member;
    }

    public static Feed createFeed(Member member, String content) {
        return new Feed(member, getEmptyStringIfContentNull(content));
    }

    public void updateFeedImage(List<FeedImage> feedImages) {
        this.images = feedImages;
    }

    public void editFeed(String content, List<FeedImage> feedImages) {
        this.content = getEmptyStringIfContentNull(content);
        this.images = feedImages;
    }

    private static String getEmptyStringIfContentNull(String content) {
        return Optional.ofNullable(content).orElse("");
    }
}
