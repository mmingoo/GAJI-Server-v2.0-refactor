package gaji.service.domain.roomBoard.repository.RoomPost;

import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPostBookmark;
import gaji.service.domain.studyMate.entity.StudyMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomPostBookmarkRepository extends JpaRepository<RoomPostBookmark,Long> {
    Optional<RoomPostBookmark> findByRoomPostAndStudyMate(RoomPost post, StudyMate studyMate);
}
