package gaji.service.domain.studyMate.entity;

import gaji.service.domain.enums.Role;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPost;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPostLikes;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePost;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePostBookmark;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePostLike;
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
public class StudyMate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;


    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomNotice> roomNoticeList = new ArrayList<>();


    //  트러블 슈팅
    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomTroublePostLike> roomTroublePostLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomTroublePostBookmark> roomTroublePostBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomTroublePost> roomTroublePostList = new ArrayList<>();


    //정보나눔
    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomInfoPost> roomInfoPostList = new ArrayList<>();


    //게시글
    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomPost> roomPostList = new ArrayList<>();

    @OneToMany(mappedBy = "studyMate", cascade =  CascadeType.ALL)
    private List<RoomPostLikes> roomPostLikesList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public StudyMate(User user, Room room, Role role) {
        this.user = user;
        this.room = room;
        this.role = role;
    }
}
