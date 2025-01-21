package gaji.service.domain.roomBoard.service.RoomInfo;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomInfo.InfoPostComment;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPost;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.InfoPostCommentRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.RoomInfoPostRepository;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.service.StudyMateQueryService;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomInfoPostQueryServiceImpl implements RoomInfoPostQueryService{

    private final RoomInfoPostRepository roomInfoPostRepository;
    private final InfoPostCommentRepository infoPostCommentRepository;
    private final StudyMateQueryService studyMateQueryService;
    private final RoomBoardRepository roomBoardRepository;
    @Override
    public RoomInfoPost findInfoPostById(Long PostId){
        return roomInfoPostRepository.findById(PostId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._POST_NOT_FOUND));
    }


    @Override
    public InfoPostComment findCommentByCommentId(Long commentId){
        return infoPostCommentRepository.findById(commentId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    public InfoPostComment findPostCommentById(Long troublePostId) {
        return infoPostCommentRepository.findById(troublePostId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    public List<RoomPostResponseDto.InfoPostSummaryDto> getNextPosts(Long roomId, Long lastPostId, int size) {
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId, RoomPostType.ROOM_INFORMATION_POST)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._ROOM_BOARD_NOT_FOUND));

        LocalDateTime lastCreatedAt;
        if (lastPostId == 0) {
            lastCreatedAt = LocalDateTime.now();
        } else {
            lastCreatedAt = roomInfoPostRepository.findCreatedAtByIdOrEarliest(roomBoard.getId(), lastPostId)
                    .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        }

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        return roomInfoPostRepository.findInfoPostSummariesForInfiniteScroll(roomBoard.getId(), lastCreatedAt, pageable);
    }


    @Override
    @Transactional
    public RoomPostResponseDto.RoomInfoPostDetailDTO getPostDetail(Long postId, Long userId, int page, int size) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._POST_NOT_FOUND));

        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, post.getRoomBoard().getRoom().getId());

        post.increaseViewCnt();

        RoomPostResponseDto.RoomInfoPostDetailDTO dto = new RoomPostResponseDto.RoomInfoPostDetailDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setAuthorName(post.getStudyMate().getUser().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setBookmarkCount(post.getBookmarkCount());
        dto.setLiked(post.getRoomInfoPostLikesList().stream()
                .anyMatch(like -> like.getStudyMate().getId().equals(studyMate.getId())));
        dto.setBookmarked(post.getRoomInfoPostBookmarkList().stream()
                .anyMatch(bookmark -> bookmark.getStudyMate().getId().equals(studyMate.getId())));

        Page<RoomPostResponseDto.CommentWithRepliesDTO> comments = getCommentsWithReplies(postId, PageRequest.of(page, size));
        dto.setComments(comments);

        return dto;
    }

    @Override
    public Page<RoomPostResponseDto.CommentWithRepliesDTO> getCommentsWithReplies(Long postId, Pageable pageable) {
        Page<InfoPostComment> commentPage = infoPostCommentRepository.findOldestComments(postId, pageable);

        List<RoomPostResponseDto.CommentWithRepliesDTO> commentDTOs = commentPage.getContent().stream()
                .map(this::convertToCommentWithRepliesDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(commentDTOs, pageable, commentPage.getTotalElements());
    }

    private RoomPostResponseDto.CommentWithRepliesDTO convertToCommentWithRepliesDTO(InfoPostComment comment) {
        RoomPostResponseDto.CommentWithRepliesDTO dto = new RoomPostResponseDto.CommentWithRepliesDTO();
        dto.setId(comment.getId());
        dto.setUserNickName(comment.getUser().getName());
        dto.setCommentBody(comment.getBody());
        dto.setCommentWriteDate(comment.getCreatedAt());
        dto.setReplies(comment.getReplies().stream()
                .sorted(Comparator.comparing(InfoPostComment::getCreatedAt))
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private RoomPostResponseDto.CommentDTO convertToCommentDTO(InfoPostComment reply) {
        RoomPostResponseDto.CommentDTO dto = new RoomPostResponseDto.CommentDTO();
        dto.setId(reply.getId());
        dto.setUserNickName(reply.getUser().getName());
        dto.setCommentBody(reply.getBody());
        dto.setCommentWriteDate(reply.getCreatedAt());
        return dto;
    }
}
