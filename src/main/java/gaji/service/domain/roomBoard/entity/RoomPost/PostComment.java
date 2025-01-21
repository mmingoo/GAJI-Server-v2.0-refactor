package gaji.service.domain.roomBoard.entity.RoomPost;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PostComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private RoomPost roomPost;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> replies = new ArrayList<>();

    private boolean isReply;

    public void updateComment(String body) {
        this.body = body;
    }

    public boolean isAuthor(Long userId) {
        return this.user.getId().equals(userId);
    }

    public void addReply(PostComment reply) {
        if (!this.isReply) {
            this.replies.add(reply);
            reply.setParentComment(this);
            reply.setIsReply(true);
        }
    }

    public void setParentComment(PostComment parentComment) {
        this.parentComment = parentComment;
    }

    public void setIsReply(boolean isReply) {
        this.isReply = isReply;
    }
}