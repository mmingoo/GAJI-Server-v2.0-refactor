package gaji.service.domain.post.entity;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommnuityPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostBookmark> postBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostFile> postFileList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLikes> postLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body; // TODO: 게시글 text 제한 20000자
    private int hit; // TODO: Integer vs int 고민해보기
    private int likeCnt;
    private int bookmarkCnt;
    private int commentCnt;
    @Getter(AccessLevel.NONE)
    private int commentGroupNum;
    private String thumbnailUrl;
    private Integer popularityScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatusEnum status;

    @Builder
    public CommnuityPost(User user, String title, String body, String thumbnailUrl, PostTypeEnum type, PostStatusEnum status) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
        this.status = status;
    }

    // 엔티티 생성 시 초기값 설정
    @PrePersist
    public void prePersist() {
        this.hit = 0;
        this.likeCnt = 0;
        this.bookmarkCnt = 0;
        this.commentCnt = 0;
        this.commentGroupNum = 0;
        this.popularityScore = 0;
    }

    public static String getDefaultThumbnailUrl() {
        return "가지 로고 url";
    }

    public int getCommentGroupNum() {
        return this.commentGroupNum++;
    }

    public void increaseBookmarkCnt() {
        this.bookmarkCnt++;
    }

    public void decreaseBookmarkCnt() {
        this.bookmarkCnt--;
    }

    public void increaseLikeCnt() {
        this.likeCnt++;
    }

    public void decreaseLikeCnt() {
        this.likeCnt--;
    }

    public void increaseHitCnt() {
        this.hit++;
    }

    public void increasePopularityScoreByLike() {
        this.popularityScore += 2;
    }

    public void decreasePopularityScoreByLike() {
        this.popularityScore -= 2;
    }

    public void increasePopularityScoreByHit() {
        this.popularityScore++;
    }

    public void increaseCommentCnt() {
        this.commentCnt++;
    }

    public void decreaseCommentCnt() {
        this.commentCnt--;
    }

    public void updateStatus(PostStatusEnum status) {
        this.status = status;
    }
}
