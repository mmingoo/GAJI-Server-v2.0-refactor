package gaji.service.domain.studyMate.service;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.user.entity.User;

public interface StudyMateQueryService {
    StudyMate findByUserIdAndRoomId(Long id, Long roomId);

    StudyMate findById(Long studyMateId);

    boolean existsByUserAndRoom(User user, Room room);

    boolean checkLeader(User user, Room room);
}
