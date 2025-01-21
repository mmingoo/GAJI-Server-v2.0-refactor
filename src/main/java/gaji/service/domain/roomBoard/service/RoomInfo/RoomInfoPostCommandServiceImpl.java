package gaji.service.domain.roomBoard.service.RoomInfo;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.converter.RoomPostConverter;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomInfo.InfoPostComment;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPost;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPostBookmark;
import gaji.service.domain.roomBoard.entity.RoomInfo.RoomInfoPostLikes;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.InfoPostCommentRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.RoomInfoPostBookmarkRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.RoomInfoPostLikesRepository;
import gaji.service.domain.roomBoard.repository.RoomInfo.RoomInfoPostRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostLikesRepository;
import gaji.service.domain.roomBoard.web.dto.RoomPostRequestDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.service.StudyMateQueryService;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomInfoPostCommandServiceImpl implements RoomInfoPostCommandService {
    private final RoomInfoPostRepository roomInfoPostRepository;
    private final RoomBoardRepository roomBoardRepository;
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;
    private final StudyMateQueryService studyMateQueryService;
    private final RoomInfoPostQueryService roomInfoPostQueryService;
    private final InfoPostCommentRepository infoPostCommentRepository;
    private final RoomPostLikesRepository roomPostLikesRepository;
    private final RoomInfoPostLikesRepository roomInfoPostLikesRepository;
    private final RoomInfoPostBookmarkRepository roomInfoPostBookmarkRepository;

    @Override
    public RoomPostResponseDto.toCreateRoomInfoPostIdDTO createRoomInfoPostIdDTO(Long roomId, Long userId, RoomPostRequestDto.RoomInfoPostDto requestDto) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(user.getId(), roomId);

        // 스터디룸 게시판 확인 또는 생성
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId, RoomPostType.ROOM_INFORMATION_POST)
                .orElseGet(() -> {
                    RoomBoard newRoomBoard = RoomBoard.builder()
                            .room(room)
                            .roomPostType(RoomPostType.ROOM_INFORMATION_POST)
                            .name(room.getName())
                            .build();
                    return roomBoardRepository.save(newRoomBoard);
                });


        RoomInfoPost roomInfoPost = RoomPostConverter.toRoomInfoPost(requestDto, studyMate,roomBoard);
        roomInfoPostRepository.save(roomInfoPost);

        return RoomPostConverter.infoPostIdDto(roomInfoPost.getId());
    }

    @Override
    public void updateInfoPost(Long postId, Long userId, RoomPostRequestDto.RoomInfoPostDto requestDto) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_UPDATE_AUTH);
        }

        post.update(requestDto.getTitle(), requestDto.getBody());
    }


    @Override
    public void deleteInfoPost(Long postId, Long userId) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND);
        }
        roomInfoPostRepository.delete(post);
    }

    @Override
    public RoomPostResponseDto.toWriteCommentDto writeCommentOnInfoPost(Long userId, Long postId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        User user = userQueryService.findUserById(userId);
        RoomInfoPost roomPost = roomInfoPostQueryService.findInfoPostById(postId);

        InfoPostComment postComment = InfoPostComment.builder()
                .user(user)
                .roomInfoPost(roomPost)
                .body(request.getBody())
                .build();
         infoPostCommentRepository.save(postComment);

        return RoomPostConverter.toWriteInfoPostCommentDto(postComment);
    }

    @Override
    public void updateComment(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto requestDto) {
        InfoPostComment comment = roomInfoPostQueryService.findCommentByCommentId(commentId);
        if (!comment.isAuthor(userId)){
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_COMMENT_UPDATE_AUTH);
        }
        comment.updateComment(requestDto.getBody());
    }


    @Override
    public void deleteComment(Long commentId, Long userId) {
        InfoPostComment comment = roomInfoPostQueryService.findPostCommentById(commentId);

        if (!comment.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_COMMENT_DELETE_AUTH);
        }

        if (comment.isReply()) {
            // 답글인 경우, 해당 답글만 삭제
            deleteReply(comment);
        } else {
            // 댓글인 경우, 댓글과 모든 관련 답글 삭제
            deleteCommentAndReplies(comment);
        }


    }

    private void deleteReply(InfoPostComment reply) {
        InfoPostComment parentComment = reply.getParentComment();
        parentComment.getReplies().remove(reply);
        infoPostCommentRepository.delete(reply);
    }

    private void deleteCommentAndReplies(InfoPostComment comment) {
        // CascadeType.ALL과 orphanRemoval = true 설정으로 인해
        // 댓글을 삭제하면 연관된 모든 답글도 자동으로 삭제됩니다.
        infoPostCommentRepository.delete(comment);
    }

    @Override
    public void addLike(Long postId, Long userId, Long roomId) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        Optional<RoomInfoPostLikes> likeOptional = roomInfoPostLikesRepository
                .findByRoomInfoPostAndStudyMate(post, studyMate);

        if (likeOptional.isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_LIKED);
        }

        RoomInfoPostLikes newLike = RoomInfoPostLikes.builder()
                .roomInfoPost(post)
                .studyMate(studyMate)
                .build();

        post.addLike(newLike);
        roomInfoPostLikesRepository.save(newLike);
    }

    @Override
    public void removeLike(Long postId, Long userId, Long roomId) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomInfoPostLikes like = roomInfoPostLikesRepository
                .findByRoomInfoPostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus. _POST_LIKE_NOT_FOUND));

        post.removeLike(like);
        roomInfoPostLikesRepository.delete(like);
    }
    @Override
    public void addBookmark(Long postId, Long userId, Long roomId) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        if (roomInfoPostBookmarkRepository.findByRoomInfoPostAndStudyMate(post, studyMate).isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_BOOKMARKED);
        }

        RoomInfoPostBookmark newBookmark = RoomInfoPostBookmark.builder()
                .roomInfoPost(post)
                .studyMate(studyMate)
                .build();
        post.addBookmark(newBookmark);
        roomInfoPostBookmarkRepository.save(newBookmark);

    }

    @Override
    public void removeBookmark(Long postId, Long userId, Long roomId) {
        RoomInfoPost post = roomInfoPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomInfoPostBookmark bookmark = roomInfoPostBookmarkRepository
                .findByRoomInfoPostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_BOOKMARKED_NOT_FOUND));

        post.removeBookmark(bookmark);
        roomInfoPostBookmarkRepository.delete(bookmark);
    }

    @Override
    public InfoPostComment addReply(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        InfoPostComment parentComment = infoPostCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));

        if (parentComment.isReply()) {
            throw new IllegalStateException("답글에는 답글을 달 수 없습니다.");
        }

        User user = userQueryService.findUserById(userId);
        InfoPostComment reply = InfoPostComment.builder()
                .user(user)
                .roomInfoPost(parentComment.getRoomInfoPost())
                .body(request.getBody())
                .isReply(true)
                .parentComment(parentComment)
                .build();

        parentComment.addReply(reply);
        return infoPostCommentRepository.save(reply);
    }
}

