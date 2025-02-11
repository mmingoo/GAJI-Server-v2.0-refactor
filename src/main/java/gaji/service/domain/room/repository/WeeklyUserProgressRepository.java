package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.studyMate.entity.WeeklyUserProgress;
import gaji.service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyUserProgressRepository extends JpaRepository<WeeklyUserProgress, Long> {
    Optional<WeeklyUserProgress> findByRoomEventAndUser(RoomEvent roomEvent, User user);

    @Query("SELECT w.user.id as userId, w.user.nickname as nickname, w.progressPercentage as progressPercentage " +
            "FROM WeeklyUserProgress w WHERE w.roomEvent.id = :roomEventId")
    List<UserProgressProjection> findProgressByRoomEventId(@Param("roomEventId") Long roomEventId);

    interface UserProgressProjection {
        Long getUserId();
        String getNickname();
        Double getProgressPercentage();
    }
}
