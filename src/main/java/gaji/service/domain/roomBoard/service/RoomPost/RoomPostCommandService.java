package gaji.service.domain.roomBoard.service.RoomPost;

import gaji.service.domain.roomBoard.entity.RoomPost.PostComment;
import gaji.service.domain.roomBoard.web.dto.RoomPostRequestDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;

public interface RoomPostCommandService {
    RoomPostResponseDto.toCreateRoomPostIdDTO createRoomPost(Long roomId, Long userId, RoomPostRequestDto.RoomPostDto requestDto);
    void updatePost(Long postId, Long userId, RoomPostRequestDto.RoomPostDto requestDto);

    void deletePost(Long postId, Long userId);

    RoomPostResponseDto.toWriteCommentDto writeCommentOnPost(Long userId, Long postId, RoomPostRequestDto.RoomTroubleCommentDto request);

    void updateComment(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto requestDto);

    void deleteComment(Long commentId, Long userId);

    void addLike(Long postId, Long userId, Long roomId);

    void removeLike(Long postId, Long userId, Long roomId);

    void addBookmark(Long postId, Long userId, Long roomId);

    void removeBookmark(Long postId, Long userId, Long roomId);

    PostComment addReply(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto request);
}
