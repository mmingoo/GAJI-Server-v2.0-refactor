package gaji.service.domain.roomBoard.web.controller;

import gaji.service.domain.roomBoard.converter.RoomPostConverter;
import gaji.service.domain.roomBoard.entity.RoomInfo.InfoPostComment;
import gaji.service.domain.roomBoard.service.RoomInfo.RoomInfoPostCommandService;
import gaji.service.domain.roomBoard.service.RoomInfo.RoomInfoPostQueryService;
import gaji.service.domain.roomBoard.web.dto.RoomPostRequestDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.global.base.BaseResponse;
import gaji.service.jwt.service.TokenProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-rooms")
@Tag(name = "스터디룸 정보 나눔 게시판 APi", description = "스터디룸 정보 나눔 게시판 API")
public class RoomInfoPostController {
    private final TokenProviderService tokenProviderService;
    private final RoomInfoPostCommandService roomInfoPostCommandService;
    private final RoomInfoPostQueryService roomInfoPostQueryService;

    @PostMapping("/info/{roomId}")
    @Operation(summary = "스터디룸 정보나눔 게시판 등록 API")
    public BaseResponse<RoomPostResponseDto.toCreateRoomInfoPostIdDTO> StudyRoomInfoPostController(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomPostRequestDto.RoomInfoPostDto requestDto,
            @PathVariable Long roomId
    ){

        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.toCreateRoomInfoPostIdDTO roomInfoPostIdDTO = roomInfoPostCommandService.createRoomInfoPostIdDTO(roomId, userId, requestDto);
        return BaseResponse.onSuccess(roomInfoPostIdDTO);
    }

    @PutMapping("/info/{postId}")
    @Operation(summary = "스터디룸 정보나눔 게시글 업데이트 API")
    public BaseResponse<String> updatePost(
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomPostRequestDto.RoomInfoPostDto requestDto,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.updateInfoPost(postId, userId,requestDto);
        return BaseResponse.onSuccess( "게시글이 성공적으로 업데이트되었습니다.");
    }

    @DeleteMapping("/info/{postId}")
    @Operation(summary = "스터디룸 정보나눔 게시글 삭제 API")
    public BaseResponse<String> deletePost(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.deleteInfoPost(postId, userId);
        return BaseResponse.onSuccess( "게시글이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/info/{postId}/comments")
    @Operation(summary = "스터디룸 정보나눔 댓글 등록 API")
    public BaseResponse<RoomPostResponseDto.toWriteCommentDto> writeCommentOnInfoPost(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomPostRequestDto.RoomTroubleCommentDto requestDto,
            @PathVariable Long postId
    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.toWriteCommentDto newComment = roomInfoPostCommandService.writeCommentOnInfoPost(userId, postId, requestDto);
        return BaseResponse.onSuccess(newComment);
    }

    @PutMapping("/info/comments/{commentId}")
    @Operation(summary = "스터디룸 정보나눔 댓글 업데이트 API")
    public BaseResponse<String> updateComment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomPostRequestDto.RoomTroubleCommentDto requestDto,
            @PathVariable Long commentId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.updateComment(commentId, userId,requestDto);
        return BaseResponse.onSuccess( "댓글이 성공적으로 업데이트되었습니다.");
    }

    @DeleteMapping("/info/comments/{commentId}")
    @Operation(summary = "스터디룸 정보나눔 게시글 댓글 삭제 API")
    public BaseResponse<String> deleteComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.deleteComment(commentId, userId);
        return BaseResponse.onSuccess( "댓글이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/info/{roomId}/posts/{postId}/like")
    @Operation(summary = "스터디룸 정보나눔 게시글 좋아요 API")
    public BaseResponse<String> postLike(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @PathVariable Long roomId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.addLike(postId, userId, roomId);
        return BaseResponse.onSuccess("LIKE");
    }

    @DeleteMapping("/info/{roomId}/posts/{postId}/unlike")
    @Operation(summary = "스터디룸 정보나눔 게시글 좋아요 취소 API")
    public BaseResponse<String> postUnlike(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @PathVariable Long roomId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.removeLike(postId, userId, roomId);
        return BaseResponse.onSuccess("UNLIKE");
    }

    @PostMapping("/info/{roomId}/{postId}/bookmark-add")
    @Operation(summary = "정보나눔 게시글 북마크 추가 API")
    public BaseResponse<String> addBookmark(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long roomId,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.addBookmark(postId, userId, roomId);
        return BaseResponse.onSuccess( "북마크가 성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/info/{roomId}/{postId}/bookmark-remove")
    @Operation(summary = "정보나눔 게시글 북마크 제거 API")
    public BaseResponse<String> removeBookmark(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long roomId,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomInfoPostCommandService.removeBookmark(postId, userId, roomId);
        return BaseResponse.onSuccess( "북마크가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/{roomId}/info/list")
    @Operation(summary = "게시글 무한 스크롤 조회", description = "게시글을 무한 스크롤 방식으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public BaseResponse<List<RoomPostResponseDto.InfoPostSummaryDto>> getNextInfoPosts(
            @PathVariable @Parameter(description = "스터디룸 ID") Long roomId,
            @RequestParam @Parameter(description = "마지막으로 로드된 게시글 ID") Long lastPostId,
            @RequestParam(defaultValue = "10") @Parameter(description = "조회할 게시글 수") int size) {
        List<RoomPostResponseDto.InfoPostSummaryDto> posts =
                roomInfoPostQueryService.getNextPosts(roomId, lastPostId, size);
        return BaseResponse.onSuccess(posts);
    }

    @PostMapping("/info/comments/{commentId}/replies")
    @Operation(summary = "게시글 댓글의 답글 작성 API")
    public BaseResponse<RoomPostResponseDto.toWriteCommentDto> addReply(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId,
            @RequestBody @Valid RoomPostRequestDto.RoomTroubleCommentDto requestDto
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        InfoPostComment replyComment = roomInfoPostCommandService.addReply(commentId, userId, requestDto);
        return BaseResponse.onSuccess(RoomPostConverter.toWriteInfoPostCommentDto(replyComment));
    }

    @GetMapping("/info/detail/{postId}")
    @Operation(summary = "정보나눔 게시글 상세 조회", description = "1페이지를 불러오고 싶다면 page : 0으로 입력해야 합니다. ex) page 0 > 1페이지 / page 1 > 2페이지 ")
    public BaseResponse<RoomPostResponseDto.RoomInfoPostDetailDTO> getPostDetail(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.RoomInfoPostDetailDTO postDetail = roomInfoPostQueryService.getPostDetail(postId, userId, page, size);
        return BaseResponse.onSuccess(postDetail);
    }

    @GetMapping("/info/{postId}/get/comments")
    @Operation(summary = "정보나눔 게시글 댓글 및 답글 추가 로딩", description = "추가로 댓글을 무한 스크롤 형태로 불러올 때 호출하는 api 입니다")
    public BaseResponse<Page<RoomPostResponseDto.CommentWithRepliesDTO>> getMoreComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RoomPostResponseDto.CommentWithRepliesDTO> comments = roomInfoPostQueryService.getCommentsWithReplies(postId, PageRequest.of(page, size));
        return BaseResponse.onSuccess(comments);
    }
}
