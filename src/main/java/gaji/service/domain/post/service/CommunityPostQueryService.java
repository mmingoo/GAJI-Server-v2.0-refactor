package gaji.service.domain.post.service;

import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.post.entity.CommunityPost;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;


public interface CommunityPostQueryService {

    CommunityPost findPostByPostId(Long postId);
    CommunityPostResponseDTO.PostPreviewListDTO getPostList(String keyword,
                            Integer lastPopularityScore,
                            Long lastPostId,
                            Integer lastLikeCnt,
                            Integer lastHit,
                            PostTypeEnum postType,
                            String category,
                            SortType sortType,
                            PostStatusEnum filter,
                            int page,
                            int size);
    CommunityPostResponseDTO.PostDetailDTO getPostDetail(Long userId, Long postId);
    boolean isPostWriter(Long userId, CommunityPost post);
    void validPostWriter(Long userId, CommunityPost post);
    void validExistsPostLikes(Long userId, CommunityPost post);
    void validExistsPostBookmark(Long userId, CommunityPost post);

    String putPostStatus(Long userId, Long postId);
}
