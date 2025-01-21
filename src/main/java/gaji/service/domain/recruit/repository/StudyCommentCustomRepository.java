package gaji.service.domain.recruit.repository;

import gaji.service.domain.recruit.entity.StudyComment;
import gaji.service.domain.room.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StudyCommentCustomRepository {
    Slice<StudyComment> findByRoomFetchJoinWithUser(
            Integer lastCommentOrder, Integer lastDepth, Long lastCommentId, Room room, Pageable pageable);
}
