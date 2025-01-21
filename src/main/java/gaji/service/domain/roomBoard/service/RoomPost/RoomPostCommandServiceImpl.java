package gaji.service.domain.roomBoard.service.RoomPost;

import gaji.service.domain.enums.RoomPostType;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.roomBoard.code.RoomPostErrorStatus;
import gaji.service.domain.roomBoard.converter.RoomPostConverter;
import gaji.service.domain.roomBoard.entity.RoomBoard;
import gaji.service.domain.roomBoard.entity.RoomPost.PostComment;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPostBookmark;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPostLikes;
import gaji.service.domain.roomBoard.repository.RoomBoardRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.PostCommentRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostBookmarkRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostLikesRepository;
import gaji.service.domain.roomBoard.repository.RoomPost.RoomPostRepository;
import gaji.service.domain.roomBoard.web.dto.RoomPostRequestDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.repository.StudyMateRepository;
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
public class RoomPostCommandServiceImpl implements RoomPostCommandService {
    private final RoomPostRepository roomPostRepository;
    private final RoomBoardRepository roomBoardRepository;
    private final StudyMateRepository studyMateRepository;
    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;
    private final StudyMateQueryService studyMateQueryService;
    private final PostCommentRepository postCommentRepository;
    private final RoomPostQueryService roomPostQueryService;
    private final RoomPostLikesRepository roomPostLikesRepository;
    private final RoomPostBookmarkRepository roomPostBookmarkRepository;

    @Override
    public RoomPostResponseDto.toCreateRoomPostIdDTO createRoomPost(Long roomId, Long userId, RoomPostRequestDto.RoomPostDto requestDto) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(user.getId(), roomId);

        // 스터디룸 게시판 확인 또는 생성
        RoomBoard roomBoard = roomBoardRepository.findRoomBoardByRoomIdAndRoomPostType(roomId,RoomPostType.ROOM_POST)
                .orElseGet(() -> {
                    RoomBoard newRoomBoard = RoomBoard.builder()
                            .room(room)
                            .name(room.getName())
                            .roomPostType(RoomPostType.ROOM_POST)
                            .build();
                    return roomBoardRepository.save(newRoomBoard);
                });

        // RoomPost 생성 및 저장
        RoomPost roomPost = RoomPostConverter.toRoomPost(requestDto, studyMate, roomBoard);
        roomPostRepository.save(roomPost);

        return RoomPostConverter.postIdDto(roomPost.getId());
    }

    @Override
    public void updatePost(Long postId, Long userId, RoomPostRequestDto.RoomPostDto requestDto) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));

        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_UPDATE_AUTH);
        }

        post.update(requestDto.getTitle(), requestDto.getBody());
    }


    @Override
    public void deletePost(Long postId, Long userId) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        if (!post.isAuthor(userId)) {
            throw new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND);
        }
        roomPostRepository.delete(post);
    }


    @Override
    public RoomPostResponseDto.toWriteCommentDto writeCommentOnPost(Long userId, Long postId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        User user = userQueryService.findUserById(userId);
        RoomPost roomPost = roomPostQueryService.findPostById(postId);

        PostComment postComment = PostComment.builder()
                .user(user)
                .roomPost(roomPost)
                .body(request.getBody())
                .build();
        postCommentRepository.save(postComment);

        return RoomPostConverter.toWritePostCommentDto(postComment);
    }

    @Override
    public void updateComment(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto requestDto) {
        PostComment comment = roomPostQueryService.findCommentByCommentId(commentId);
        if (!comment.isAuthor(userId)){
            throw new RestApiException(RoomPostErrorStatus._USER_NOT_COMMENT_UPDATE_AUTH);
        }

        comment.updateComment(requestDto.getBody());
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        PostComment comment = roomPostQueryService.findPostCommentById(commentId);

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

        private void deleteReply(PostComment reply) {
            PostComment parentComment = reply.getParentComment();
            parentComment.getReplies().remove(reply);
            postCommentRepository.delete(reply);
        }

        private void deleteCommentAndReplies(PostComment comment) {
            // CascadeType.ALL과 orphanRemoval = true 설정으로 인해
            // 댓글을 삭제하면 연관된 모든 답글도 자동으로 삭제됩니다.
            postCommentRepository.delete(comment);
        }

    @Override
    public void addLike(Long postId, Long userId, Long roomId) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        Optional<RoomPostLikes> likeOptional = roomPostLikesRepository
                .findByRoomPostAndStudyMate(post, studyMate)
                ;

        if (likeOptional.isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_LIKED);
        }

        RoomPostLikes newLike = RoomPostLikes.builder()
                .roomPost(post)
                .studyMate(studyMate)
                .build();

        post.addLike(newLike);
        roomPostLikesRepository.save(newLike);
    }

    @Override
    public void removeLike(Long postId, Long userId, Long roomId) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomPostLikes like = roomPostLikesRepository
                .findByRoomPostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus. _POST_LIKE_NOT_FOUND));

        post.removeLike(like);
        roomPostLikesRepository.delete(like);
    }

    @Override
    public void addBookmark(Long postId, Long userId, Long roomId) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        if (roomPostBookmarkRepository.findByRoomPostAndStudyMate(post, studyMate).isPresent()) {
            throw new RestApiException(RoomPostErrorStatus._POST_ALREADY_BOOKMARKED);
        }

        RoomPostBookmark newBookmark = RoomPostBookmark.builder()
                .roomPost(post)
                .studyMate(studyMate)
                .build();
        post.addBookmark(newBookmark);
        roomPostBookmarkRepository.save(newBookmark);

    }

    @Override
    public void removeBookmark(Long postId, Long userId, Long roomId) {
        RoomPost post = roomPostRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_NOT_FOUND));
        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);

        RoomPostBookmark bookmark = roomPostBookmarkRepository
                .findByRoomPostAndStudyMate(post, studyMate)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._POST_BOOKMARKED_NOT_FOUND));

        post.removeBookmark(bookmark);
        roomPostBookmarkRepository.delete(bookmark);
    }

    @Override
    public PostComment addReply(Long commentId, Long userId, RoomPostRequestDto.RoomTroubleCommentDto request) {
        PostComment parentComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(RoomPostErrorStatus._NOT_FOUND_COMMENT));

        if (parentComment.isReply()) {
            throw new IllegalStateException("답글에는 답글을 달 수 없습니다.");
        }

        User user = userQueryService.findUserById(userId);
        PostComment reply = PostComment.builder()
                .user(user)
                .roomPost(parentComment.getRoomPost())
                .body(request.getBody())
                .isReply(true)
                .parentComment(parentComment)
                .build();

        parentComment.addReply(reply);
        return postCommentRepository.save(reply);
    }

}
