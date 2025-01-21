package gaji.service.domain.roomBoard.web.controller;

import gaji.service.domain.roomBoard.converter.RoomPostConverter;
import gaji.service.domain.roomBoard.entity.RoomTrouble.TroublePostComment;
import gaji.service.domain.roomBoard.service.RoomTrouble.RoomTroublePostCommandService;
import gaji.service.domain.roomBoard.service.RoomTrouble.RoomTroublePostQueryService;
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
@Tag(name = "스터디룸 트러블슈팅 게시판 APi", description = "스터디룸 트러블슈팅 게시판 API")
public class RoomTroublePostController {

    private final TokenProviderService tokenProviderService;
    private final RoomTroublePostCommandService roomTroublePostCommandService;
    private final RoomTroublePostQueryService roomTroublePostQueryService;
    @PostMapping("/trouble/{roomId}")
    @Operation(summary = "스터디룸 트러블슈팅 게시판 등록 API")
    public BaseResponse<RoomPostResponseDto.toCreateRoomTroublePostIdDTO> TroublePostController(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomPostRequestDto.RoomTroubloePostDto requestDto,
            @PathVariable Long roomId
    ){

        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.toCreateRoomTroublePostIdDTO roomTroublePostIdDTO = roomTroublePostCommandService.createRoomTroublePost(roomId, userId, requestDto);
        return BaseResponse.onSuccess(roomTroublePostIdDTO);
    }

    @PostMapping("/trouble/{troublePostId}/comments")
    @Operation(summary = "스터디룸 트러블슈팅 댓글 등록 API")
    public BaseResponse<RoomPostResponseDto.toWriteCommentDto> writeCommentOnTroublePost(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid RoomPostRequestDto.RoomTroubleCommentDto requestDto,
            @PathVariable Long troublePostId
    ){
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.toWriteCommentDto newComment = roomTroublePostCommandService.writeCommentOnTroublePost(userId, troublePostId, requestDto);
        return BaseResponse.onSuccess(newComment);
    }

    @PostMapping("/trouble/{roomId}/posts/{postId}/like")
    @Operation(summary = "스터디룸 트러블슈팅 게시글 좋아요 API")
    public BaseResponse<String> postLike(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @PathVariable Long roomId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.addLike(postId, userId, roomId);
        return BaseResponse.onSuccess("LIKE");
    }

    @DeleteMapping("/trouble/{roomId}/posts/{postId}/unlike")
    @Operation(summary = "스터디룸 트러블슈팅 게시글 좋아요 취소 API")
    public BaseResponse<String> postUnlike(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @PathVariable Long roomId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.removeLike(postId, userId, roomId);
        return BaseResponse.onSuccess("UNLIKE");
    }


    @DeleteMapping("/trouble/{postId}")
    @Operation(summary = "스터디룸 트러블슈팅 게시글 삭제 API")
    public BaseResponse<String> deletePost(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.deletePost(postId, userId);
        return BaseResponse.onSuccess( "게시글이 성공적으로 삭제되었습니다.");
    }

    @PutMapping("/trouble/{postId}")
    @Operation(summary = "스터디룸 트러블슈팅 게시글 업데이트 API")
    public BaseResponse<String> updatePost(
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomPostRequestDto.RoomTroubloePostDto requestDto,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.updatePost(postId, userId,requestDto);
        return BaseResponse.onSuccess( "게시글이 성공적으로 업데이트되었습니다.");
    }

    @PutMapping("/trouble/comments/{commentId}")
    @Operation(summary = "스터디룸 트러블슈팅 댓글 업데이트 API")
    public BaseResponse<String> updateComment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody RoomPostRequestDto.RoomTroubleCommentDto requestDto,
            @PathVariable Long commentId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.updateComment(commentId, userId,requestDto);
        return BaseResponse.onSuccess( "댓글이 성공적으로 업데이트되었습니다.");
    }

    @DeleteMapping("/trouble/comments/{commentId}")
    @Operation(summary = "스터디룸 트러블슈팅 댓글 삭제 API")
    public BaseResponse<String> deleteComment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.deleteComment(commentId, userId);
        return BaseResponse.onSuccess( "댓글이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/trouble/{roomId}/{postId}/bookmark-add")
    @Operation(summary = "트러블 슈팅 게시글 북마크 추가 API")
    public BaseResponse<String> addBookmark(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long roomId,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.addBookmark(postId, userId, roomId);
        return BaseResponse.onSuccess( "북마크가 성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/trouble/{roomId}/{postId}/bookmark-remove")
    @Operation(summary = "트러블 슈팅 게시글 북마크 제거 API")
    public BaseResponse<String> removeBookmark(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long roomId,
            @PathVariable Long postId
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        roomTroublePostCommandService.removeBookmark(postId, userId, roomId);
        return BaseResponse.onSuccess( "북마크가 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/trouble/comments/{commentId}/replies")
    @Operation(summary = "트러블 슈팅 게시글 댓글의 답글 작성 API")
    public BaseResponse<RoomPostResponseDto.toWriteCommentDto> addReply(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long commentId,
            @RequestBody @Valid RoomPostRequestDto.RoomTroubleCommentDto requestDto
    ) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        TroublePostComment replyComment = roomTroublePostCommandService.addReply(commentId, userId, requestDto);
        return BaseResponse.onSuccess(RoomPostConverter.toWriteCommentDto(replyComment));
    }

    @GetMapping("/{roomId}/trouble/list")
    @Operation(summary = "트러블 슈팅 게시글 무한 스크롤 조회", description = "트러블 슈팅 게시글을 무한 스크롤 방식으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public BaseResponse<List<RoomPostResponseDto.TroublePostSummaryDto>> getNextTroublePosts(
            @PathVariable @Parameter(description = "게시판 ID") Long roomId,
            @RequestParam @Parameter(description = "마지막으로 로드된 게시글 ID") Long lastPostId,
            @RequestParam(defaultValue = "10") @Parameter(description = "조회할 게시글 수") int size) {
        List<RoomPostResponseDto.TroublePostSummaryDto> posts;
        posts = roomTroublePostQueryService.getNextTroublePosts(roomId, lastPostId, size);
        return BaseResponse.onSuccess(posts);
    }

    @GetMapping("/trouble/detail/{postId}")
    @Operation(summary = "트러블 슈팅 게시글 상세 조회", description = "1페이지를 불러오고 싶다면 page : 0으로 입력해야 합니다. ex) page 0 > 1페이지 / page 1 > 2페이지 ")
    public BaseResponse<RoomPostResponseDto.TroublePostDetailDTO> getPostDetail(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = tokenProviderService.getUserIdFromToken(authorization);
        RoomPostResponseDto.TroublePostDetailDTO postDetail = roomTroublePostQueryService.getPostDetail(postId, userId, page, size);
        return BaseResponse.onSuccess(postDetail);
    }

    @GetMapping("/trouble/{postId}/comments")
    @Operation(summary = "트러블 슈팅 게시글 댓글 및 답글 추가 로딩", description = "추가로 댓글을 무한 스크롤 형태로 불러올 때 호출하는 api 입니다")
    public BaseResponse<Page<RoomPostResponseDto.CommentWithRepliesDTO>> getMoreComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RoomPostResponseDto.CommentWithRepliesDTO> comments = roomTroublePostQueryService.getCommentsWithReplies(postId, PageRequest.of(page, size));
        return BaseResponse.onSuccess(comments);
    }
}




