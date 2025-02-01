package gaji.service.domain.roomBoard.service.RoomPost;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomPost.PostComment;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.PostCommentRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostQueryRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostRepository;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.service.StudyMateQueryService;
import gaji.service.global.converter.DateConverter;
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
public class RoomPostQueryServiceImpl implements RoomPostQueryService {

    private final RoomPostQueryRepository roomPostQueryRepository;
    private final RoomPostRepository roomPostRepository;
    private final PostCommentRepository postCommentRepository;
    private final StudyMateQueryService studyMateQueryService;
    private final RoomBoardRepository roomBoardRepository;

    @Override
    public List<RoomPostResponseDto.PostListDto> getTop3RecentPosts(Long roomId) {
        return roomPostQueryRepository.findTop3RecentPostsWithUserInfo(roomId);
    }

    @Override
    public List<RoomPostResponseDto.MainPostSummaryDto> getLatestPosts(Long roomId) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId, RoomPostType.ROOM_POST)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._ROOM_BOARD_NOT_FOUND));

        List<RoomPostResponseDto.MainPostSummaryDto> posts = roomPostRepository.findLatestPostsSummary(roomBoard.getId(), pageable);

        LocalDateTime now = LocalDateTime.now();
        for (RoomPostResponseDto.MainPostSummaryDto post : posts) {
            post.setTimeSincePosted(DateConverter.convertToRelativeTimeFormat(post.getCreatedAt()));
        }

        return posts;
    }

    @Override
    public List<RoomPostResponseDto.PostSummaryDto> getNextPosts(Long roomId, Long lastPostId, int size) {
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId, RoomPostType.ROOM_POST)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._ROOM_BOARD_NOT_FOUND));

        LocalDateTime lastCreatedAt;
        if (lastPostId == 0) {
            lastCreatedAt = LocalDateTime.now();
        } else {
            lastCreatedAt = roomPostRepository.findCreatedAtByIdOrEarliest(roomBoard.getId(), lastPostId)
                    .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        }

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        return roomPostRepository.findPostSummariesForInfiniteScroll(roomBoard.getId(), lastCreatedAt, pageable);
    }
    @Override
    public RoomPost findPostById(Long PostId){
        return roomPostRepository.findById(PostId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._POST_NOT_FOUND));
    }

    @Override
    public PostComment findCommentByCommentId(Long commentId){
        return postCommentRepository.findById(commentId)
                .orElseThrow(() ->new RestApiException( RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    public PostComment findPostCommentById(Long troublePostId) {
        return postCommentRepository.findById(troublePostId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));
    }

    @Override
    @Transactional
    public RoomPostResponseDto.RoomPostDetailDTO getPostDetail(Long postId, Long userId, int page, int size) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, post.getRoomBoard().getRoom().getId());
        post.increaseViewCnt();

        RoomPostResponseDto.RoomPostDetailDTO dto = new RoomPostResponseDto.RoomPostDetailDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setAuthorName(post.getStudyMate().getUser().getNickname());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikeCount());
        dto.setBookmarkCount(post.getBookmarkCount());
        dto.setLiked(post.getRoomPostLikesList().stream()
                .anyMatch(like -> like.getStudyMate().getId().equals(studyMate.getId())));
        dto.setBookmarked(post.getRoomPostBookmarkList().stream()
                .anyMatch(bookmark -> bookmark.getStudyMate().getId().equals(studyMate.getId())));

        Page<RoomPostResponseDto.CommentWithRepliesDTO> comments = getCommentsWithReplies(postId, PageRequest.of(page, size));
        dto.setComments(comments);

        return dto;
    }

    @Override
    public Page<RoomPostResponseDto.CommentWithRepliesDTO> getCommentsWithReplies(Long postId, Pageable pageable) {
        Page<PostComment> commentPage = postCommentRepository.findOldestComments(postId, pageable);

        List<RoomPostResponseDto.CommentWithRepliesDTO> commentDTOs = commentPage.getContent().stream()
                .map(this::convertToCommentWithRepliesDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(commentDTOs, pageable, commentPage.getTotalElements());
    }

    private RoomPostResponseDto.CommentWithRepliesDTO convertToCommentWithRepliesDTO(PostComment comment) {
        RoomPostResponseDto.CommentWithRepliesDTO dto = new RoomPostResponseDto.CommentWithRepliesDTO();
        dto.setId(comment.getId());
        dto.setUserNickName(comment.getUser().getNickname());
        dto.setCommentBody(comment.getBody());
        dto.setCommentWriteDate(comment.getCreatedAt());
        dto.setReplies(comment.getReplies().stream()
                .sorted(Comparator.comparing(PostComment::getCreatedAt))
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private RoomPostResponseDto.CommentDTO convertToCommentDTO(PostComment reply) {
        RoomPostResponseDto.CommentDTO dto = new RoomPostResponseDto.CommentDTO();
        dto.setId(reply.getId());
        dto.setUserNickName(reply.getUser().getNickname());
        dto.setCommentBody(reply.getBody());
        dto.setCommentWriteDate(reply.getCreatedAt());
        return dto;
    }

}
