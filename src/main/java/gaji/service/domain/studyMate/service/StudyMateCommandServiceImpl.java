package gaji.service.domain.studyMate.service;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.repository.StudyMateRepository;
import gaji.service.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyMateCommandServiceImpl implements StudyMateCommandService{
    private final StudyMateRepository studyMateRepository;

    @Override
    @Transactional
    public void saveStudyMate(StudyMate studyMate) {
        studyMateRepository.save(studyMate);
    }

    @Override
    @Transactional
    public void deleteByUserAndRoom(User user, Room room) {
        studyMateRepository.deleteByUserAndRoom(user, room);
    }
}
