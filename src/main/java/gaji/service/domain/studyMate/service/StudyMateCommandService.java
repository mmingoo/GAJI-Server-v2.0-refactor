package gaji.service.domain.studyMate.service;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.user.entity.User;

public interface StudyMateCommandService {
    void saveStudyMate(StudyMate studyMate);

    void deleteByUserAndRoom(User user, Room room);
}
