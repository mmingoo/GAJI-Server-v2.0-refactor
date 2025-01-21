package gaji.service.domain.roomBoard.repository.RoomInfo;

import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPost;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPostBookmark;
import gaji.service.domain.studyMate.entity.StudyMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomInfoPostBookmarkRepository extends JpaRepository<RoomInfoPostBookmark,Long> {
    Optional<RoomInfoPostBookmark> findByRoomInfoPostAndStudyMate(RoomInfoPost post, StudyMate studyMate);
}
