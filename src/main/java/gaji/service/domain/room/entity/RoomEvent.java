package gaji.service.domain.room.entity;

import gaji.service.domain.studyMate.entity.Assignment;
import gaji.service.domain.studyMate.entity.WeeklyUserProgress;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "roomEvent", cascade = CascadeType.ALL)
    private final List<WeeklyUserProgress> weeklyUserProgressList = new ArrayList<>();

    @OneToMany(mappedBy = "roomEvent", cascade = CascadeType.ALL)
    private final List<Assignment> assignmentList = new ArrayList<>();


    @Column(length = 30)
    private String title;

    @Column(length = 200)
    private String description;

    private Integer weeks;

    private LocalDate startTime;

    private LocalDate endTime;

    private boolean isPublic;

    public void updateEvent(LocalDate startTime, LocalDate endTime, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

}
