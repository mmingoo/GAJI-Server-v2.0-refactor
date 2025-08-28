package gaji.service.domain.studyMate.entity;

import gaji.service.domain.enums.Role;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomNotice;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public StudyMate(User user, Room room, Role role) {
        this.user = user;
        this.room = room;
        this.role = role;
    }
}
