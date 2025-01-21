package gaji.service.domain.roomBoard.entity.RoomPost;

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
public class RoomPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyMate_id")
    private StudyMate studyMate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private RoomBoard roomBoard;

    private String title;
    private String body;

    @OneToMany(mappedBy = "roomPost",cascade = CascadeType.ALL)
    private List<RoomPostReport> roomPostReportList;

    @OneToMany(mappedBy = "roomPost",cascade = CascadeType.ALL)
    private List<RoomPostLikes> roomPostLikesList;

    @OneToMany(mappedBy = "roomPost",cascade = CascadeType.ALL)
    private final List<RoomPostBookmark> roomPostBookmarkList = new ArrayList<>() ;

    @OneToMany(mappedBy = "roomPost",cascade = CascadeType.ALL)
    private final List<RoomPostFile> roomPostFileList  = new ArrayList<>() ;

    @OneToMany(mappedBy = "roomPost",cascade = CascadeType.ALL)
    private final List<PostComment> postCommentList  = new ArrayList<>() ;

    //erd 설계에는 없지만 추가
    private int viewCount;
    private int likeCount;
    private int bookmarkCount;

    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
        this.likeCount = 0;
        this.bookmarkCount = 0;
    }
    public void setRoomBoard(RoomBoard roomBoard) {
        this.roomBoard = roomBoard;
    }

    public boolean isAuthor(Long userId){
        return this.studyMate.getUser().getId().equals(userId);
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void addLike(RoomPostLikes like) {
        this.roomPostLikesList.add(like);
        this.likeCount++;
    }

    public void removeLike(RoomPostLikes like) {
        this.roomPostLikesList.remove(like);
        this.likeCount = Math.max(0, this.likeCount - 1);

    }
    public void addBookmark(RoomPostBookmark bookmark) {
        this.roomPostBookmarkList.add(bookmark);
        this.bookmarkCount++;
    }

    public void removeBookmark(RoomPostBookmark bookmark) {
        this.roomPostBookmarkList.remove(bookmark);
        this. bookmarkCount = Math.max(0, this.bookmarkCount - 1);

    }
    public void increaseViewCnt() {
        this.viewCount++;
    }

}
