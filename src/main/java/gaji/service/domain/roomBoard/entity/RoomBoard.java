package gaji.service.domain.roomBoard.entity;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "roomBoard", cascade = CascadeType.ALL)
    private final List<RoomPost> roomPostList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    //추후 삭제합시다. RoomPostType으로 한정된 게시판을 관리한다면 필요 없을거같습니다.
    private String name;

    @Enumerated(EnumType.STRING)
    RoomPostType roomPostType;


    public void addRoomPost(RoomPost roomPost) {
        this.roomPostList.add(roomPost);
        roomPost.setRoomBoard(this);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setName(String name) {
        this.name = name;
    }

}
