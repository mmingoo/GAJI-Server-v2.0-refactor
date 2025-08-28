package gaji.service.domain.post.entity;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.enums.CommentStatus;
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
public class CommunityComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post;

    //자기 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommunityComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> replies = new ArrayList<>();

    private String body;
    private Integer groupNum;
    private int depth;
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Builder
    public CommunityComment(User user, CommunityPost post, CommunityComment parent, String body) {
        this.user = user;
        this.post = post;
        this.parent = parent;
        this.body = body;
        this.status = CommentStatus.PUBLIC; // 댓글은 기본상태
        this.depth = (parent == null) ? 0 : 1; // 부모 댓글이 있으면 depth = 1
        this.groupNum = (parent == null) ? post.getCommentGroupNum() : parent.getGroupNum(); // groupNum은 부모 댓글이 있으면 부모 댓글과 같은 값, 없으면 증가
    }
}