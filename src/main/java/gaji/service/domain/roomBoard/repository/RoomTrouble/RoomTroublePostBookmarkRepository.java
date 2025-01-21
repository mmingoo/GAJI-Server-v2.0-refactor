package gaji.service.domain.roomBoard.repository.RoomTrouble;

import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePost;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePostBookmark;
import gaji.service.domain.studyMate.entity.StudyMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTroublePostBookmarkRepository extends JpaRepository<RoomTroublePostBookmark, Long> {
    Optional<RoomTroublePostBookmark> findByRoomTroublePostAndStudyMate(RoomTroublePost post, StudyMate studyMate);
}
