package gaji.service.domain.post.web.controller;

import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.post.converter.CommunityCommentConverter;
import gaji.service.domain.post.converter.CommunityPostConverter;
import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.service.CommunityCommentService;
import gaji.service.domain.post.service.CommunityPostCommandService;
import gaji.service.domain.post.service.CommunityPostQueryService;
import gaji.service.domain.post.web.dto.CommunityPostCommentResponseDTO;
import gaji.service.domain.post.web.dto.CommunityPostRequestDTO;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;
import gaji.service.global.base.BaseResponse;
import gaji.service.jwt.service.TokenProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community-posts")
@Tag(name = "커뮤니티 APi", description = "커뮤니티 API")
public class CommunityPostRestController {

    private final CommunityPostCommandService communityPostCommandService;
    private final CommunityPostQueryService communityPostQueryService;
    private final CommunityCommentService commentService;
    private final TokenProviderService tokenProviderService;
    private final CommunityCommentConverter communityCommentConverter;

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "커뮤니티 게시글 업로드 API", description = "커뮤니티의 게시글을 업로드하는 API입니다. 게시글 유형과 제목, 본문 내용을 검증합니다.")
    public BaseResponse<CommunityPostResponseDTO.PostIdResponseDTO> uploadPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                               @RequestBody @Valid CommunityPostRequestDTO.UploadPostRequestDTO request) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        CommunityPostResponseDTO.PostIdResponseDTO newPost = communityPostCommandService.uploadPost(userId, request);
        return BaseResponse.onSuccess(newPost);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 수정 API", description = "커뮤니티 게시글을 수정 API입니다.")
    public BaseResponse<CommunityPostResponseDTO.PostIdResponseDTO> editPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                             @RequestBody @Valid CommunityPostRequestDTO.EditPostRequestDTO request,
                                                                             @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        CommunityPostResponseDTO.PostIdResponseDTO editedCommnuityPost = communityPostCommandService.editPost(userId, postId, request);
        return BaseResponse.onSuccess(editedCommnuityPost);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 삭제 API", description = "커뮤니티 게시글을 삭제하는 API입니다. 게시글과 관련된 북마크, 좋아요, 댓글 내역을 모두 삭제합니다.(hard delete)")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse hardDeleteCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
            @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        communityPostCommandService.hardDeleteCommunityPost(userId, postId);
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "커뮤니티 게시글 상세 조회 API", description = "댓글을 제외한 게시글의 상세 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse<CommunityPostResponseDTO.PostDetailDTO> getPostDetail(@Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId,
                                                                              @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Long userId = (authorizationHeader == null) ? null : tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(communityPostQueryService.getPostDetail(userId, postId));
    }

    @GetMapping("/preivew")
    @Operation(summary = "커뮤니티 게시글 미리보기 목록 조회 API", description = "hot 게시글, 커뮤니티 게시글 미리보기 목록, 검색 API에 모두 사용 가능합니다.")
    @Parameters({
            @Parameter(name = "lastPopularityScore", description = "마지막으로 조회한 게시글의 인기 점수"),
            @Parameter(name = "lastPostId", description = "마지막으로 조회한 게시글의 id"),
            @Parameter(name = "lastLikeCnt", description = "마지막으로 조회한 게시글의 좋아요 수"),
            @Parameter(name = "lastHit", description = "마지막으로 조회한 게시글의 조회수"),
            @Parameter(name = "postType", description = "게시글의 유형(블로그, 프로젝트, 질문)"),
            @Parameter(name = "category", description = "카테고리"),
            @Parameter(name = "sortType", description = "정렬 유형(hot, recent, like, hit)"),
            @Parameter(name = "filter", description = "게시글의 상태(모집중, 모집완료, 미완료질문, 해결완료)"),
    })
    public BaseResponse<CommunityPostResponseDTO.PostPreviewListDTO> getPostPreviewList(@RequestParam(required = false) String keyword,
                                                                                        @Min(value = 0, message = "lastPopularityScore는 0 이상 이어야 합니다.") @RequestParam(required = false) Integer lastPopularityScore,
                                                                                        @Min(value = 1, message = "lastPostId는 1 이상 이어야 합니다.") @RequestParam(required = false) Long lastPostId,
                                                                                        @Min(value = 0, message = "lastLikeCnt는 0 이상 이어야 합니다.") @RequestParam(required = false) Integer lastLikeCnt,
                                                                                        @Min(value = 0, message = "lastHit은 0 이상 이어야 합니다.") @RequestParam(required = false) Integer lastHit,
                                                                                        @RequestParam(required = false) PostTypeEnum postType,
                                                                                        @RequestParam(required = false) String category,
                                                                                        @RequestParam(required = false, defaultValue = "recent") SortType sortType,
                                                                                        @RequestParam(required = false) PostStatusEnum filter,
                                                                                        @Min(value = 0, message = "page는 0 이상 이어야 합니다.") @RequestParam(defaultValue = "0") int page,
                                                                                        @Min(value = 1, message = "size는 1 이상 이어야 합니다.") @RequestParam(defaultValue = "10") int size) {

        return BaseResponse.onSuccess(communityPostQueryService.getPostList(keyword, lastPopularityScore, lastPostId, lastLikeCnt, lastHit, postType, category, sortType, filter, page, size));
    }

    @PostMapping("/{postId}/comments")
    @Operation(summary = "커뮤니티 게시글 댓글 작성 API", description = "커뮤니티의 게시글에 댓글을 작성하는 API입니다. 대댓글을 작성하는 거라면 Long 타입의 parentCommentId를 query parameter로 보내주시면 됩니다!")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
            @Parameter(name = "parentCommentId", description = "부모 댓글의 id, 대댓글 작성할 때 필요한 부모 댓글의 id입니다."),
    })
    public BaseResponse<CommunityPostCommentResponseDTO.WriteCommentResponseDTO> writeCommentOnCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                                                             @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId,
                                                                                                             @Min(value = 1, message = "parentCommentId는 1 이상 이어야 합니다.") @RequestParam(required = false) Long parentCommentId,
                                                                                                             @RequestBody @Valid CommunityPostRequestDTO.WriteCommentRequestDTO request) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(communityPostCommandService.writeCommentOnCommunityPost(userId, postId, parentCommentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "커뮤니티 게시글 댓글 삭제 API", description = "커뮤니티 게시글의 댓글을 삭제하는 API입니다.(hard delete)")
    @Parameters({
            @Parameter(name = "commentId", description = "댓글 id"),
    })
    public BaseResponse hardDeleteComment(@RequestHeader("Authorization") String authorizationHeader,
                                          @Min(value = 1, message = "commentId는 1 이상 이어야 합니다.") @PathVariable Long commentId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        communityPostCommandService.hardDeleteComment(userId, commentId);
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "커뮤니티 게시글 댓글 목록 조회 API", description = "lastGroupNum에 마지막으로 조회한 댓글의 grounNum과, size로 조회할 데이터의 개수를 보내주세요.")
    public BaseResponse<CommunityPostCommentResponseDTO.PostCommentListDTO> getCommentList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                                           @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId,
                                                                                           @Min(value = 0, message = "lastGroupNum은 0 이상 이어야 합니다.") @RequestParam(required = false) Integer lastGroupNum, // 마지막 댓글 ID
                                                                                           @Min(value = 0, message = "page는 0 이상 이어야 합니다.") @RequestParam(defaultValue = "0") int page,
                                                                                           @Min(value = 1, message = "size는 1 이상 이어야 합니다.") @RequestParam(defaultValue = "10") int size) // 페이지 크기 (기본값 10))
    {
        Long userId = (authorizationHeader == null) ? null : tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(commentService.getCommentListByPost(userId, postId, lastGroupNum, page, size));
    }

    @PostMapping("/{postId}/bookmarks")
    @Operation(summary = "커뮤니티 게시글 북마크 API", description = "커뮤니티 게시글을 북마크하는 API입니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse<CommunityPostResponseDTO.PostBookmarkIdDTO> bookmarkCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                                          @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(communityPostCommandService.bookmarkCommunityPost(userId, postId));
    }

    @DeleteMapping("/{postId}/bookmarks")
    @Operation(summary = "커뮤니티 게시글 북마크 취소 API", description = "커뮤니티 게시글을 북마크 취소하는 API입니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse cancelBookmarkCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
            @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        communityPostCommandService.cancelbookmarkCommunityPost(userId, postId);
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/{postId}/likes")
    @Operation(summary = "커뮤니티 게시글 좋아요 API", description = "커뮤니티 게시글을 좋아요하는 API입니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse<CommunityPostResponseDTO.PostLikesIdDTO> likeCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                                   @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        return BaseResponse.onSuccess(communityPostCommandService.likeCommunityPost(userId, postId));
    }

    @DeleteMapping("/{postId}/likes")
    @Operation(summary = "커뮤니티 게시글 좋아요 취소 API", description = "커뮤니티 게시글을 좋아요 취소하는 API입니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 id"),
    })
    public BaseResponse cancelLikeCommunityPost(@RequestHeader("Authorization") String authorizationHeader,
            @Min(value = 1, message = "postId는 1 이상 이어야 합니다.") @PathVariable Long postId) {
        Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
        communityPostCommandService.cancelLikeCommunityPost(userId, postId);
        return BaseResponse.onSuccess(null);
    }
}
