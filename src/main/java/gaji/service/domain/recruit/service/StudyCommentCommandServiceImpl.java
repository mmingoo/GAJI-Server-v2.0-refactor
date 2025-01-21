package gaji.service.domain.recruit.service;

import gaji.service.domain.enums.CommentStatus;
import gaji.service.domain.recruit.code.RecruitErrorStatus;
import gaji.service.domain.recruit.converter.RecruitConverter;
import gaji.service.domain.recruit.entity.StudyComment;
import gaji.service.domain.recruit.repository.StudyCommentRepository;
import gaji.service.domain.recruit.web.dto.RecruitRequestDTO;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCommentCommandServiceImpl implements StudyCommentCommandService{

    private final StudyCommentRepository studyCommentRepository;
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;

    @Override
    public RecruitResponseDTO.WriteCommentResponseDTO writeComment(Long userId, Long roomId, Long parentCommentId, RecruitRequestDTO.CommentContentDTO request) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        StudyComment comment = createComment(request, user, room, parentCommentId);
        studyCommentRepository.save(comment);
        room.increaseCommentCount();
        return RecruitConverter.toWriteCommentDTO(comment);
    }

    @Override
    public RecruitResponseDTO.UpdateCommentResponseDTO updateComment(Long userId, Long commentId, RecruitRequestDTO.CommentContentDTO request) {

        StudyComment comment = studyCommentRepository.findById(commentId).orElseThrow(() -> new RestApiException(RecruitErrorStatus._COMMENT_NOT_FOUND));

        if (!(comment.getUser().getId().equals(userId))) {
            throw new RestApiException(RecruitErrorStatus._COMMENT_NOT_OWNER);
        }

        comment.update(request.getBody());

        studyCommentRepository.save(comment);
        return RecruitConverter.toUpdateCommentDTO(comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        StudyComment comment = studyCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(RecruitErrorStatus._COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)) {
            throw new RestApiException(RecruitErrorStatus._COMMENT_NOT_OWNER);
        }
        if (comment.getStatus() == CommentStatus.DELETE) {
            throw new RestApiException(RecruitErrorStatus._COMMENT_ALREADY_DELETE);
        } else {
            studyCommentRepository.deleteById(commentId);
        }
    }

    private StudyComment createComment(
            RecruitRequestDTO.CommentContentDTO request, User user, Room room, Long parentCommentId) {
        if (parentCommentId != null) {
            StudyComment parentComment = studyCommentRepository.findById(parentCommentId).
                    orElseThrow(()->new RestApiException(RecruitErrorStatus._COMMENT_NOT_FOUND));
            if (parentComment.getParent() != null) {
                parentComment = parentComment.getParent();
            }
            return RecruitConverter.toComment(request, user, room, parentComment);
        } else {
            return RecruitConverter.toComment(request, user, room, null);
        }
    }


    @Override
    public void deleteByRoom(Room room) {
        studyCommentRepository.deleteAllByRoom(room);
    }
}
