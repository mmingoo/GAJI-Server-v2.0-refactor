package gaji.service.domain.room.entity;

import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.enums.RecruitPostTypeEnum;
import gaji.service.domain.recruit.entity.RecruitPostBookmark;
import gaji.service.domain.recruit.entity.RecruitPostLikes;
import gaji.service.domain.recruit.web.dto.RecruitRequestDTO;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.studyMate.entity.StudyApplicant;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 모집 게시글 작성 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 스터디룸 관련 매핑
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomEvent> roomEventList = new ArrayList<>();

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
//    private List<Assignment> assignmentList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<StudyApplicant> studyApplicantList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<StudyMate> studyMateList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomBoard> roomBoardList = new ArrayList<>();

    // 모집 게시글 관련
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Material> materialList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade =  CascadeType.ALL)
    private List<RecruitPostBookmark> recruitPostBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RecruitPostLikes> recruitPostLikesList = new ArrayList<>();

    @Column(length = 20)
    private String name;
    private String description;
    //조회수
    private int views;
    //좋아요수
    private int likes;
    //북마크수
    private int bookmarks;
    //댓글수
    private int commentCount;

    private RecruitPostTypeEnum recruitPostTypeEnum;

    private LocalDate recruitStartDay;
    private LocalDate recruitEndDay;

    private LocalDate studyStartDay;
    private LocalDate studyEndDay;

    // 썸네일 경로
    private String thumbnailUrl;

    // True : 공개 // False : 비공개
    private boolean isPrivate;

    // 초대코드
    @Column(length = 20)
    private String inviteCode;

    // 현재 스터디 참여 인원
    private int headCount;

    // 인원제한 여부 Ture : 제한있음 / False : 제한없음
    private boolean peopleLimited;

    // 최대 인원
    private int peopleMaximum;

    // 조회수 추가
    public void addView() {
        this.views++;
    }

    // 스터디 자료 추가
    public void addMaterial(Material material) {
        materialList.add(material);
    }

    @Builder
    public Room(User user, String name, String description, String thumbnailUrl, LocalDate recruitStartDay, LocalDate recruitEndDay, LocalDate studyStartDay, LocalDate studyEndDay, boolean isPrivate, String inviteCode, int headCount, int peopleMaximum, boolean peopleLimited) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.recruitStartDay = recruitStartDay;
        this.recruitEndDay = recruitEndDay;
        this.studyStartDay = studyStartDay;
        this.studyEndDay = studyEndDay;
        this.isPrivate = isPrivate;
        this.inviteCode = inviteCode;
        this.headCount = headCount;
        this.peopleMaximum = peopleMaximum;
        this.peopleLimited = peopleLimited;
    }

    @PrePersist
    public void prePersist() {
        this.views = 0;
        this.likes = 0;
        this.bookmarks = 0;
        this.commentCount = 0;
        this.recruitPostTypeEnum = RecruitPostTypeEnum.RECRUITING;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }
    public void increaseLike() {
        this.likes++;
    }

    public void decreaseLike() {
        this.likes--;
    }

    public void increaseBookmark() {
        this.bookmarks++;
    }

    public void decreaseBookmark() {
        this.bookmarks--;
    }

    public void update(RecruitRequestDTO.RoomContentDTO request, String thumbnailUrl, String inviteCode, int peopleMaximum) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.thumbnailUrl = thumbnailUrl;
        this.recruitStartDay = request.getRecruitStartDay();
        this.recruitEndDay = request.getRecruitEndDay();
        this.studyStartDay = request.getStudyStartDay();
        this.studyEndDay = request.getStudyEndDay();
        this.isPrivate = request.isPrivate();
        this.inviteCode = inviteCode;
        this.peopleLimited = request.isPeopleLimited();
        this.peopleMaximum = peopleMaximum;
    }

    public void updateRecruitStatus(RecruitPostTypeEnum recruitType) {
        this.recruitPostTypeEnum = recruitType;
    }
}
