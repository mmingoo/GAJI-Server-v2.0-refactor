package gaji.service.domain.roomBoard.service.RoomTrouble;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePost;
import gaji.service.domain.roomBoard.entity.RoomTrouble.TroublePostComment;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.RoomTroublePostRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.TroublePostCommentRepository;
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
public class RoomTroublePostQueryServiceImpl implements RoomTroublePostQueryService {

    private final TroublePostCommentRepository troublePostCommentRepository;
    private final RoomTroublePostRepository roomTroublePostRepository;
    private final StudyMateQueryService studyMateQueryService;
    private final RoomBoardRepository roomBoardRepository;

    @Override
    public TroublePostComment findCommentByCommentId(Long commentId){
        return troublePostCommentRepository.findById(commentId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    public TroublePostComment findTroublePostCommentById(Long troublePostId) {
        return troublePostCommentRepository.findById(troublePostId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    public List<RoomPostResponseDto.TroublePostSummaryDto> getNextTroublePosts(Long roomId, Long lastPostId, int size) {
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId, RoomPostType.ROOM_TROUBLE_POST)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._ROOM_BOARD_NOT_FOUND));

        LocalDateTime lastCreatedAt;
        if (lastPostId == 0) {
            lastCreatedAt = LocalDateTime.now();
        } else {
            lastCreatedAt = roomTroublePostRepository.findCreatedAtByIdOrEarliest(roomBoard.getId(), lastPostId)
                    .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        }

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        return roomTroublePostRepository.findTroublePostSummariesForInfiniteScroll(roomBoard.getId(), lastCreatedAt, pageable);
    }



    @Override
    @Transactional
    public RoomPostResponseDto.TroublePostDetailDTO getPostDetail(Long postId, Long userId, int page, int size) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, post.getRoomBoard().getRoom().getId());
        post.increaseViewCnt();
        RoomPostResponseDto.TroublePostDetailDTO dto = new RoomPostResponseDto.TroublePostDetailDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setAuthorName(post.getStudyMate().getUser().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setBookmarkCount(post.getBookmarkCount());
        dto.setLiked(post.getRoomTroublePostLikeList().stream()
                .anyMatch(like -> like.getStudyMate().getId().equals(studyMate.getId())));
        dto.setBookmarked(post.getRoomTroublePostBookmarkList().stream()
                .anyMatch(bookmark -> bookmark.getStudyMate().getId().equals(studyMate.getId())));

        Page<RoomPostResponseDto.CommentWithRepliesDTO> comments = getCommentsWithReplies(postId, PageRequest.of(page, size));
        dto.setComments(comments);

        return dto;
    }

    @Override
    public Page<RoomPostResponseDto.CommentWithRepliesDTO> getCommentsWithReplies(Long postId, Pageable pageable) {
        Page<TroublePostComment> commentPage = troublePostCommentRepository.findOldestComments(postId, pageable);

        List<RoomPostResponseDto.CommentWithRepliesDTO> commentDTOs = commentPage.getContent().stream()
                .map(this::convertToCommentWithRepliesDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(commentDTOs, pageable, commentPage.getTotalElements());
    }

    private RoomPostResponseDto.CommentWithRepliesDTO convertToCommentWithRepliesDTO(TroublePostComment comment) {
        RoomPostResponseDto.CommentWithRepliesDTO dto = new RoomPostResponseDto.CommentWithRepliesDTO();
        dto.setId(comment.getId());
        dto.setUserNickName(comment.getUser().getName());
        dto.setCommentBody(comment.getBody());
        dto.setCommentWriteDate(comment.getCreatedAt());
        dto.setReplies(comment.getReplies().stream()
                .sorted(Comparator.comparing(TroublePostComment::getCreatedAt))
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private RoomPostResponseDto.CommentDTO convertToCommentDTO(TroublePostComment reply) {
        RoomPostResponseDto.CommentDTO dto = new RoomPostResponseDto.CommentDTO();
        dto.setId(reply.getId());
        dto.setUserNickName(reply.getUser().getName());
        dto.setCommentBody(reply.getBody());
        dto.setCommentWriteDate(reply.getCreatedAt());
        return dto;
    }
}
