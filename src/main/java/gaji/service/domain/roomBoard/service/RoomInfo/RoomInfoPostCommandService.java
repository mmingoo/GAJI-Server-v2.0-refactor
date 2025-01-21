package gaji.service.domain.roomBoard.service.RoomInfo;

import gaji.service.domain.roomBoard.entity.RoomInfo.InfoPostComment;
import gaji.service.domain.roomBoard.web.dto.RoomPostRequestDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;

public interface RoomInfoPostCommandService {
    RoomPostResponseDto.toCreateRoomInfoPostIdDTO createRoomInfoPostIdDTO(Long roomId, Long userId, RoomPostRequestDto.RoomInfoPostDto requestDto);

    void updateInfoPost(Long postId, Long userId, RoomPostRequestDto.RoomInfoPostDto requestDto);

    void deleteInfoPost(Long postId, Long userId);

    RoomPostResponseDto.toWriteCommentDto writeCommentOnInfoPost(Long userId, Long postId, RoomPostRequestDto.RoomTroubleCommentDto request);

    void updateComment(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto requestDto);

    void deleteComment(Long commentId, Long userId);

    void addLike(Long postId, Long userId, Long roomId);

    void removeLike(Long postId, Long userId, Long roomId);

    void addBookmark(Long postId, Long userId, Long roomId);

    void removeBookmark(Long postId, Long userId, Long roomId);

    InfoPostComment addReply(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto request);
}
