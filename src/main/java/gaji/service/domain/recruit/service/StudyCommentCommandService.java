package gaji.service.domain.recruit.service;

import gaji.service.domain.recruit.web.dto.RecruitRequestDTO;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Room;

public interface StudyCommentCommandService {

    RecruitResponseDTO.WriteCommentResponseDTO writeComment(
            Long userId, Long roomId, Long parentCommentId, RecruitRequestDTO.CommentContentDTO request);

    RecruitResponseDTO.UpdateCommentResponseDTO updateComment(
            Long userId, Long commentId, RecruitRequestDTO.CommentContentDTO request);

    void deleteComment(Long userId, Long commentId);

    void deleteByRoom(Room room);
}


