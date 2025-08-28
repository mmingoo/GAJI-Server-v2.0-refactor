package gaji.service.domain.studyMate.service;

import gaji.service.domain.enums.Role;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.studyMate.code.StudyMateErrorStatus;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.repository.StudyMateRepository;
import gaji.service.domain.user.entity.User;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyMateQueryServiceImpl implements StudyMateQueryService {

    private final StudyMateRepository studyMateRepository;

    @Override
    public StudyMate findByUserIdAndRoomId(Long userId, Long roomId) {
        return studyMateRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new RestApiException(StudyMateErrorStatus._USER_NOT_IN_STUDYROOM));

    }

    @Override
    public StudyMate findById(Long studyMateId){
        return studyMateRepository.findById(studyMateId)
                .orElseThrow(() -> new RestApiException(StudyMateErrorStatus._USER_NOT_IN_STUDYROOM));
    }

    @Override
    public boolean existsByUserAndRoom(User user, Room room) {
        return studyMateRepository.existsByUserAndRoom(user, room);
    }

    @Override
    public boolean checkLeader(User user, Room room) {
        StudyMate studyMate = findByUserIdAndRoomId(user.getId(), room.getId());
        return studyMate.getRole() == Role.READER;
    }

}
