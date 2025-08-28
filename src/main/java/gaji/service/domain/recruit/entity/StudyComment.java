package gaji.service.domain.recruit.entity;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.enums.CommentStatus;
import gaji.service.domain.room.entity.Room;
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
public class StudyComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private StudyComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyComment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "studyComment", cascade = CascadeType.ALL)
    private List<StudyCommentLikes> studyCommentLikes = new ArrayList<>();

    private String body;
    private Integer depth; // 댓글의 깊이 (대댓글 여부)
    private Integer commentOrder; // 댓글의 순서 (같은 부모면 같은 순서)

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Builder
    public StudyComment(User user, Room room, StudyComment parentComment, String body) {
        this.user = user;
        this.room = room;
        this.parent = parentComment;
        this.body = body;
        this.status = CommentStatus.PUBLIC;
        if (parentComment == null) {
            this.depth = 0;
            this.commentOrder = room.getCommentCount();
        } else {
            this.depth = 1;
            this.commentOrder = parent.getCommentOrder();
        }
    }

    public void update(String body) {
        this.body = body;
        this.status = CommentStatus.UPDATED;
    }
}
