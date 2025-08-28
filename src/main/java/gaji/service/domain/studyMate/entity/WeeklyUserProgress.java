package gaji.service.domain.studyMate.entity;

import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class WeeklyUserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_event_id")
    private RoomEvent roomEvent;

    private Double progressPercentage;

    private Integer totalAssignments;

    private Integer completedAssignments;

    public static WeeklyUserProgress createInitialProgress(User user, RoomEvent roomEvent, int totalAssignments) {
        return WeeklyUserProgress.builder()
                .user(user)
                .roomEvent(roomEvent)
                .progressPercentage(0.0)
                .totalAssignments(totalAssignments)
                .completedAssignments(0)
                .build();
    }

    public void updateProgress(int completedAssignments) {
        this.completedAssignments = completedAssignments;
        this.progressPercentage = totalAssignments > 0
                ? ((double) completedAssignments / totalAssignments) * 100
                : 0.0;
    }

    public static WeeklyUserProgress createEmpty() {
        return new WeeklyUserProgress();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoomEvent(RoomEvent roomEvent) {
        this.roomEvent = roomEvent;
    }

    public void setTotalAssignments(int totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public void setCompletedAssignments(int completedAssignments) {
        this.completedAssignments = completedAssignments;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
