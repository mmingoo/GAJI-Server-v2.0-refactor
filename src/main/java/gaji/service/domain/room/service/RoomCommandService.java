package gaji.service.domain.room.service;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.room.web.dto.RoomRequestDto;
import gaji.service.domain.room.web.dto.response.RoomResponseDto;
import gaji.service.domain.room.web.dto.response.ToggleAssignmentResponseDto;
import gaji.service.domain.studyMate.entity.Assignment;
import gaji.service.domain.studyMate.entity.WeeklyUserProgress;
import gaji.service.domain.user.entity.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface RoomCommandService {
    @Transactional
    void createUserAssignmentsForStudyMembers(Assignment assignment);

    //과제생성1
    List<Assignment> createAssignment(Long roomId, Long userId, Integer weeks, RoomRequestDto.AssignmentDto requestDto);

    @Transactional
    RoomNotice createNotice(Long roomId, Long userId, RoomRequestDto.RoomNoticeDto requestDto);

    Assignment updateAssignment(Long assignmentId, String newBody);

    void deleteAssignment(Long assignmentId);

    RoomEvent setStudyPeriod(Long roomId, Integer weeks, Long userId, RoomRequestDto.StudyPeriodDto requestDto);

    RoomEvent setStudyDescription(Long roomId, Integer weeks, Long userId, RoomRequestDto.StudyDescriptionDto requestDto);

    RoomEvent updateRoomEvent(Long roomId, Integer weeks, RoomRequestDto.RoomEventUpdateDTO updateDTO);

    boolean toggleNoticeConfirmation(Long roomId, Long noticeId, Long userId);

    void saveRoom(Room room);

    void deleteRoom(Room room);

    RoomResponseDto.AssignmentProgressResponse toggleAssignmentCompletion(Long userId, Long userAssignmentId);

    WeeklyUserProgress calculateAndSaveProgress(RoomEvent roomEvent, User user);

    void deleteRoomNotice(Long noticeId, Long userId);

    RoomNotice updateRoomNotice(Long noticeId, Long userId, String description);

    ToggleAssignmentResponseDto getToggleAssignment(Long userId, Long roomId, Integer weeks);

    Long kickoutStudyRoom(Long userId, Long roomId);
}
