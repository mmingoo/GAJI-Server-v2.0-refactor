package gaji.service.domain.studyMate.entity;

import gaji.service.domain.room.entity.RoomEvent;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_event_id")
    private RoomEvent roomEvent;

    @Column(length = 30)
    private String body;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserAssignment> userAssignmentList = new ArrayList<>();

    public void updateBody(String body) {
        this.body = body;
    }

    public void addUserAssignment(UserAssignment userAssignment) {
        this.userAssignmentList.add(userAssignment);
        userAssignment.setAssignment(this);
    }

    public void removeUserAssignment(UserAssignment userAssignment) {
        this.userAssignmentList.remove(userAssignment);
        userAssignment.setAssignment(null);
    }
}
