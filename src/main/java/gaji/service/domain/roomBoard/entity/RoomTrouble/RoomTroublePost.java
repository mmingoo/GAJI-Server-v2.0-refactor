package gaji.service.domain.roomBoard.entity.RoomTrouble;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.studyMate.entity.StudyMate;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomTroublePost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private int viewCount;
    private int likeCount;
    private int bookmarkCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private RoomBoard roomBoard;

    @OneToMany(mappedBy = "roomTroublePost", cascade = CascadeType.ALL)
    private List<TroublePostComment> troublePostCommentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_mate_id")
    private StudyMate studyMate;

    @OneToMany(mappedBy = "roomTroublePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomTroublePostLike> roomTroublePostLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "roomTroublePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomTroublePostBookmark> roomTroublePostBookmarkList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
        this.likeCount = 0;
        this.bookmarkCount = 0;
    }

    public void addLike(RoomTroublePostLike like) {
        this.roomTroublePostLikeList.add(like);
        this.likeCount++;
    }

    public void removeLike(RoomTroublePostLike like) {
        this.roomTroublePostLikeList.remove(like);
        this.likeCount = Math.max(0, this.likeCount - 1);

    }

    public void addBookmark(RoomTroublePostBookmark bookmark) {
        this.roomTroublePostBookmarkList.add(bookmark);
        this.bookmarkCount++;
    }

    public void removeBookmark(RoomTroublePostBookmark bookmark) {
        this.roomTroublePostBookmarkList.remove(bookmark);
        this. bookmarkCount = Math.max(0, this.bookmarkCount - 1);

    }

    public void increaseViewCnt() {
        this.viewCount++;
    }


    public boolean isAuthor(Long userId) {
        return this.studyMate.getUser().getId().equals(userId);
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

}