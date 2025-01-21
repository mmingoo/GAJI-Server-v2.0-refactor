package gaji.service.domain.roomBoard.entity.RoomInfo;

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
public class RoomInfoPost extends BaseEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_mate_id")
    private StudyMate studyMate;

    @OneToMany(mappedBy = "roomInfoPost", cascade = CascadeType.ALL)
    private List<RoomInfoPostLikes> roomInfoPostLikesList = new ArrayList<>();


    @OneToMany(mappedBy = "roomInfoPost", cascade = CascadeType.ALL)
    private List<RoomInfoPostBookmark> roomInfoPostBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "roomInfoPost", cascade =  CascadeType.ALL)
    private List<InfoPostComment> infoPostCommentList = new ArrayList<>();

//    @OneToMany(mappedBy = "roomInfoPost", cascade = CascadeType.ALL)
//    private List<InfoPostComment> infoPostCommentList = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
        this.likeCount = 0;
        this.bookmarkCount = 0;
    }

    public boolean isAuthor(Long userId){
        return this.studyMate.getUser().getId().equals(userId);
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void addLike(RoomInfoPostLikes like) {
        this.roomInfoPostLikesList.add(like);
        this.likeCount++;
    }

    public void removeLike(RoomInfoPostLikes like) {
        this.roomInfoPostLikesList.remove(like);
        this.likeCount = Math.max(0, this.likeCount - 1);

    }

    public void addBookmark(RoomInfoPostBookmark bookmark) {
        this.roomInfoPostBookmarkList.add(bookmark);
        this.bookmarkCount++;
    }

    public void removeBookmark(RoomInfoPostBookmark bookmark) {
        this.roomInfoPostBookmarkList.remove(bookmark);
        this. bookmarkCount = Math.max(0, this.bookmarkCount - 1);

    }

    public void increaseViewCnt() {
        this.viewCount++;
    }
}
