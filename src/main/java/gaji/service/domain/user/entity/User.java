package gaji.service.domain.user.entity;


import gaji.service.domain.Report;
import gaji.service.domain.common.entity.BaseEntity;
import gaji.service.domain.enums.ServiceRole;
import gaji.service.domain.enums.SocialType;
import gaji.service.domain.enums.UserActive;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.entity.PostFile;
import gaji.service.domain.post.entity.PostLikes;
import gaji.service.domain.recruit.entity.RecruitPostBookmark;
import gaji.service.domain.recruit.entity.RecruitPostLikes;
import gaji.service.domain.recruit.entity.SearchKeyword;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.studyMate.entity.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SearchKeyword> searchKeywordList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RoomEvent> roomEventList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL )
    private List<StudyMate> studyMateList;

//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL )
//    private List<RoomPostBookmark> roomPostBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL )
    private List<Room> recruitPostList;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL )
    private List<RecruitPostBookmark> recruitPostBookmarkList;

    @OneToMany(mappedBy = "user")
    private List<StudyApplicant> studyApplicantList;

    @OneToMany(mappedBy = "user")
    private List<RecruitPostLikes> recruitPostLikesList;

    @OneToMany(mappedBy = "user")
    private List<CommunityComment> commentList;

    @OneToMany(mappedBy = "user")
    private List<CommnuityPost> postList;

//    @OneToMany(mappedBy = "user")
//    private List<PostBookmark> postBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostFile> postFileList;

    @OneToMany(mappedBy = "user")
    private List<PostLikes> postLikesList;

    @OneToMany(mappedBy = "user")
    private List<Report> reportList;

    @OneToMany(mappedBy = "user")
    private List<UserAssignment> userAssignmentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WeeklyUserProgress> weeklyUserProgressList;

    @ColumnDefault("가지돌이")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private UserActive status;

    private LocalDateTime inactiveTime;

    @Enumerated(EnumType.STRING)
    private ServiceRole role;

    private String profileImagePth;

    private String usernameId;

    public void updateStatus(UserActive status) {
        this.status=status;
        this.inactiveTime=LocalDateTime.now();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Builder
    public User(String usernameId, SocialType socialType, ServiceRole role) {
        this.usernameId = usernameId;
        this.socialType = socialType;
        this.role = role;
        this.status = UserActive.ACTIVE;
        this.nickname = "가지돌이";

        // 컬렉션 필드 초기화 (null 방지)
        this.searchKeywordList = new ArrayList<>();
        this.roomEventList = new ArrayList<>();
        this.studyMateList = new ArrayList<>();
        this.recruitPostList = new ArrayList<>();
        this.recruitPostBookmarkList = new ArrayList<>();
        this.studyApplicantList = new ArrayList<>();
        this.recruitPostLikesList = new ArrayList<>();
        this.commentList = new ArrayList<>();
        this.postList = new ArrayList<>();
        this.postFileList = new ArrayList<>();
        this.postLikesList = new ArrayList<>();
        this.reportList = new ArrayList<>();
        this.userAssignmentList = new ArrayList<>();
        this.weeklyUserProgressList = new ArrayList<>();
    }


}
