package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.studyMate.entity.UserAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignment,Long> {
    Optional<RoomEvent> findRoomEventById(Long roomId);

    @Query("SELECT ua FROM UserAssignment ua WHERE ua.user.id = :userId AND ua.assignment.id = :assignmentId")
    Optional<UserAssignment> findByUserIdAndAssignmentId(@Param("userId") Long userId, @Param("assignmentId") Long assignmentId);

}
