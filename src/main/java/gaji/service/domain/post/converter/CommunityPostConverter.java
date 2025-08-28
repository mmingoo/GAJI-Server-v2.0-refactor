package gaji.service.domain.post.converter;

import gaji.service.domain.common.converter.HashtagConverter;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.common.web.dto.HashtagResponseDTO;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.post.entity.CommunityPost;
import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.entity.PostBookmark;
import gaji.service.domain.post.entity.PostLikes;
import gaji.service.domain.post.web.dto.CommunityPostRequestDTO;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;
import gaji.service.domain.user.entity.User;
import gaji.service.global.converter.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CommunityPostConverter {

    // 초기 PostStatus 지정
    public static PostStatusEnum getInitialPostStatus(PostTypeEnum type) {
        return (type == PostTypeEnum.QUESTION) ? PostStatusEnum.NEED_RESOLUTION :
                (type == PostTypeEnum.PROJECT) ? PostStatusEnum.RECRUITING : PostStatusEnum.BLOGING;
    }

    public static CommunityPostResponseDTO.PostIdResponseDTO toPostIdResponseDTO(CommunityPost post) {
        return CommunityPostResponseDTO.PostIdResponseDTO
                .builder()
                .postId(post.getId())
                .build();
    }

    public static CommunityPostResponseDTO.PostBookmarkIdDTO toPostBookmarkIdDTO(PostBookmark postBookmark) {
        return CommunityPostResponseDTO.PostBookmarkIdDTO
                .builder()
                .postBookmarkId(postBookmark.getId())
                .build();
    }

    public static CommunityPostResponseDTO.PostLikesIdDTO toPostLikesIdDTO(PostLikes postLikes) {
        return CommunityPostResponseDTO.PostLikesIdDTO
                .builder()
                .postLikesId(postLikes.getId())
                .build();
    }

    public static CommunityPost toPost(CommunityPostRequestDTO.UploadPostRequestDTO request, User user) {
        PostTypeEnum postTypeEnum = PostTypeEnum.from(request.getType());

        return CommunityPost.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .thumbnailUrl(request.getThumbnailUrl())
                .type(postTypeEnum)
                .status(getInitialPostStatus(postTypeEnum))
                .build();
    }

    public static CommunityComment toComment(CommunityPostRequestDTO.WriteCommentRequestDTO request, User user, CommunityPost post, CommunityComment parentComment) {
        return CommunityComment.builder()
                .user(user)
                .post(post)
                .parent(parentComment)
                .body(request.getBody())
                .build();
    }

    public static PostBookmark toPostBookmark(User user, CommunityPost post) {
        return PostBookmark.builder()
                .user(user)
                .post(post)
                .build();
    }

    public static PostLikes toPostLikes(User user, CommunityPost post) {
        return PostLikes.builder()
                .user(user)
                .post(post)
                .build();
    }

    public static CommunityPostResponseDTO.PostPreviewDTO toPostPreviewDTO(CommunityPost post, List<SelectHashtag> selectHashtagList) {
        List<String> hashtagList = HashtagConverter.toHashtagNameList(selectHashtagList);

        return CommunityPostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getId())
                .likeCnt(post.getLikeCnt())
                .thumbnailUrl(post.getThumbnailUrl())
                .title(post.getTitle())
                .body(post.getBody())
                .userId(post.getUser().getId())
                .userNickname(post.getUser().getNickname())
                .uploadTime(DateConverter.convertToRelativeTimeFormat(post.getCreatedAt()))
                .hit(post.getHit())
                .popularityScore(post.getPopularityScore())
                .status(post.getStatus())
                .hashtagList(hashtagList)
                .build();
    }

    public static CommunityPostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(List<CommunityPost> postList, boolean hasNext, List<SelectHashtag> selectHashtagList) {
        List<CommunityPostResponseDTO.PostPreviewDTO> postPreviewDTOList = postList.stream()
                .map(post -> CommunityPostConverter.toPostPreviewDTO(post, selectHashtagList))
                .collect(Collectors.toList());

        return CommunityPostResponseDTO.PostPreviewListDTO.builder()
                .postList(postPreviewDTOList)
                .hasNext(hasNext)
                .build();
    }

    public static CommunityPostResponseDTO.PostDetailDTO toPostDetailDTO(CommunityPost post, SelectCategory selectCategory, List<SelectHashtag> selectHashtagList,
                                                                         boolean isBookmarked, boolean isLiked, boolean isWriter) {
        List<HashtagResponseDTO.HashtagNameAndIdDTO> hashtagNameAndIdDTOList = HashtagConverter.toHashtagNameAndIdDTOList(selectHashtagList);

        return CommunityPostResponseDTO.PostDetailDTO.builder()
                .userId(post.getUser().getId())
                .type(post.getType())
                .createdAt(DateConverter.convertWriteTimeFormat(LocalDate.from(post.getCreatedAt()), ""))
                .hit(post.getHit())
                .likeCnt(post.getLikeCnt())
                .bookmarkCnt(post.getBookmarkCnt())
                .commentCnt(post.getCommentCnt())
                .userNickname(post.getUser().getNickname())
                .title(post.getTitle())
                .hashtagList(hashtagNameAndIdDTOList)
                .isWriter(isWriter)
                .bookMarkStatus(isBookmarked)
                .likeStatus(isLiked)
                .body(post.getBody())
                .status(post.getStatus())
                .category(selectCategory.getCategory().getCategory().getValue())
                .build();
    }
}
