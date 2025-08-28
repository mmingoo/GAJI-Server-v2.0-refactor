package gaji.service.domain.recruit.service;

import gaji.service.domain.recruit.converter.RecruitConverter;
import gaji.service.domain.recruit.entity.StudyComment;
import gaji.service.domain.recruit.repository.StudyCommentRepository;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyCommentQueryServiceImpl implements StudyCommentQueryService{

    private final StudyCommentRepository studyCommentRepository;
    private final RoomQueryService roomQueryService;

    @Override
    public RecruitResponseDTO.CommentListResponseDTO getCommentList(
            Long roomId, Integer lastCommentOrder, Integer lastDepth, Long lastCommentId, int size) {
        Room room = roomQueryService.findRoomById(roomId);
        PageRequest pageRequest = PageRequest.of(0, size);

        Slice<StudyComment> studyCommentList =
                studyCommentRepository.findByRoomFetchJoinWithUser(
                        lastCommentOrder, lastDepth, lastCommentId, room, pageRequest);

        int commentCount = studyCommentRepository.countByRoom(room);

        return RecruitConverter.toCommentListDTO(commentCount, studyCommentList);
    }
}
