package gaji.service.domain.roomBoard.service.RoomPost;

import gaji.service.domain.roomBoard.entity.RoomPost.PostComment;
import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomPostQueryService {
    List<RoomPostResponseDto.PostListDto> getTop3RecentPosts(Long roomId);

//    List<RoomPostResponseDto.TroublePostSummaryDto> getPaginatedTroublePosts(Long boardId, int page, int size);


    List<RoomPostResponseDto.MainPostSummaryDto> getLatestPosts(Long boardId);

    List<RoomPostResponseDto.PostSummaryDto> getNextPosts(Long roomId, Long lastPostId, int size);

    RoomPost findPostById(Long PostId);

    PostComment findCommentByCommentId(Long commentId);

    PostComment findPostCommentById(Long troublePostId);

    RoomPostResponseDto.RoomPostDetailDTO getPostDetail(Long postId, Long userId, int page, int size);

    Page<RoomPostResponseDto.CommentWithRepliesDTO> getCommentsWithReplies(Long postId, Pageable pageable);
}
