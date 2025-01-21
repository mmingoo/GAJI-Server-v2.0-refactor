package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.studyMate.entity.UserAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignment,Long> {
    Optional<RoomEvent> findRoomEventById(Long roomId);
}
