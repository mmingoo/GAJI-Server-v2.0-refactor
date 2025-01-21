package gaji.service.domain.roomBoard.service.RoomTrouble;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.converter.RoomPostConverter;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePost;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePostBookmark;
import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePostLike;
import gaji.service.domain.roomBoard.entity.RoomTrouble.TroublePostComment;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostCommentRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.RoomTroublePostBookmarkRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.RoomTroublePostLikeRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.RoomTroublePostRepository;
import gaji.service.domain.roomBoard.repository.RoomTrouble.TroublePostCommentRepository;
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
public class RoomTroublePostCommandServiceImpl implements RoomTroublePostCommandService{
    private final RoomPostRepository roomPostRepository;
    private final RoomBoardRepository roomBoardRepository;
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;
    private final StudyMateQueryService studyMateQueryService;
    private final RoomTroublePostRepository roomTroublePostRepository;
    private final RoomPostCommentRepository roomPostCommentRepository;
    private final RoomTroublePostLikeRepository roomTroublePostLikeRepository;
    private final TroublePostCommentRepository troublePostCommentRepository;
    private final RoomTroublePostBookmarkRepository roomTroublePostBookmarkRepository;
    private final RoomTroublePostQueryService roomTroublePostQueryService;

    @Override
    public RoomPostResponseDto.toWriteCommentDto writeCommentOnTroublePost(Long userId, Long postId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        User user = userQueryService.findUserById(userId);
        RoomTroublePost roomTroublePost = findTroublePostById(postId);

        TroublePostComment troublePostComment = TroublePostComment.builder()
                .user(user)
                .roomTroublePost(roomTroublePost)
                .body(request.getBody())
                .build();
        troublePostCommentRepository.save(troublePostComment);

        return RoomPostConverter.toWriteCommentDto(troublePostComment);
    }

    @Override
    public RoomPostResponseDto.toCreateRoomTroublePostIdDTO createRoomTroublePost(Long roomId, Long userId, RoomPostRequestDto.RoomTroubloePostDto requestDto) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(user.getId(), roomId);

        // 스터디룸 게시판 확인 또는 생성
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId , RoomPostType.ROOM_TROUBLE_POST)
                .orElseGet(() -> {
                    RoomBoard newRoomBoard = RoomBoard.builder()
                            .room(room)
                            .roomPostType(RoomPostType.ROOM_TROUBLE_POST)
                            .name(room.getName())
                            .build();
                    return roomBoardRepository.save(newRoomBoard);
                });

//        RoomTro roomPost = RoomPostConverter.toRoomPost(requestDto, user, roomBoard);
//        roomPost = roomPostRepository.save(roomPost);


        RoomTroublePost roomTroublePost = RoomPostConverter.toRoomTroublePost(requestDto, studyMate,roomBoard);
        roomTroublePostRepository.save(roomTroublePost);

        return RoomPostConverter.troublePostIdDto(roomTroublePost.getId());
    }

    @Override
    public RoomTroublePost findTroublePostById(Long postId){
        return roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

    }

    @Override
    public void addLike(Long postId, Long userId, Long roomId) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        Optional<RoomTroublePostLike> likeOptional = roomTroublePostLikeRepository
                .findByRoomTroublePostAndStudyMate(post, studyMate);

        if (likeOptional.isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_LIKED);
        }

        RoomTroublePostLike newLike = RoomTroublePostLike.builder()
                .roomTroublePost(post)
                .studyMate(studyMate)
                .build();

        post.addLike(newLike);
        roomTroublePostLikeRepository.save(newLike);
    }

    @Override
    public void removeLike(Long postId, Long userId, Long roomId) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomTroublePostLike like = roomTroublePostLikeRepository
                .findByRoomTroublePostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus. _POST_LIKE_NOT_FOUND));

        post.removeLike(like);
        roomTroublePostLikeRepository.delete(like);
    }

    @Override
    public void addBookmark(Long postId, Long userId, Long roomId) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        if (roomTroublePostBookmarkRepository.findByRoomTroublePostAndStudyMate(post, studyMate).isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_BOOKMARKED);
        }

        RoomTroublePostBookmark newBookmark = RoomTroublePostBookmark.builder()
                .roomTroublePost(post)
                .studyMate(studyMate)
                .build();
        post.addBookmark(newBookmark);
        roomTroublePostBookmarkRepository.save(newBookmark);

    }

    @Override
    public void removeBookmark(Long postId, Long userId, Long roomId) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomTroublePostBookmark bookmark = roomTroublePostBookmarkRepository
                .findByRoomTroublePostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_BOOKMARKED_NOT_FOUND));

        post.removeBookmark(bookmark);
        roomTroublePostBookmarkRepository.delete(bookmark);
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND);
        }
        roomTroublePostRepository.delete(post);
    }

    @Override
    public void updatePost(Long postId, Long userId, RoomPostRequestDto.RoomTroubloePostDto requestDto) {
        RoomTroublePost post = roomTroublePostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_UPDATE_AUTH);
        }

        post.update(requestDto.getTitle(), requestDto.getBody());
    }

    @Override
    public void updateComment(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto requestDto) {
        TroublePostComment comment = roomTroublePostQueryService.findCommentByCommentId(commentId);
        if (!comment.isAuthor(userId)){
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_COMMENT_UPDATE_AUTH);
        }

        comment.updateComment(requestDto.getBody());
    }


    @Override
    public void deleteComment(Long commentId, Long userId) {
        TroublePostComment comment = roomTroublePostQueryService.findTroublePostCommentById(commentId);

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





    @Override
    public TroublePostComment addReply(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        TroublePostComment parentComment = troublePostCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));

        if (parentComment.isReply()) {
            throw new IllegalStateException("답글에는 답글을 달 수 없습니다.");
        }

        User user = userQueryService.findUserById(userId);
        TroublePostComment reply = TroublePostComment.builder()
                .user(user)
                .roomTroublePost(parentComment.getRoomTroublePost())
                .body(request.getBody())
                .isReply(true)
                .parentComment(parentComment)
                .build();

        parentComment.addReply(reply);
        return troublePostCommentRepository.save(reply);
    }




    private void deleteReply(TroublePostComment reply) {
        TroublePostComment parentComment = reply.getParentComment();
        parentComment.getReplies().remove(reply);
        troublePostCommentRepository.delete(reply);
    }

    private void deleteCommentAndReplies(TroublePostComment comment) {
        // CascadeType.ALL과 orphanRemoval = true 설정으로 인해
        // 댓글을 삭제하면 연관된 모든 답글도 자동으로 삭제됩니다.
        troublePostCommentRepository.delete(comment);
    }
}
