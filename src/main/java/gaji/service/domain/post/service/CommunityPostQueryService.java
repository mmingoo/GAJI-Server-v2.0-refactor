package gaji.service.domain.post.service;

import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;
import org.springframework.data.domain.Slice;


public interface CommunityPostQueryService {

    CommnuityPost findPostByPostId(Long postId);
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
    boolean isPostWriter(Long userId, CommnuityPost post);
    void validPostWriter(Long userId, CommnuityPost post);
    void validExistsPostLikes(Long userId, CommnuityPost post);
    void validExistsPostBookmark(Long userId, CommnuityPost post);

    String putPostStatus(Long userId, Long postId);
}
