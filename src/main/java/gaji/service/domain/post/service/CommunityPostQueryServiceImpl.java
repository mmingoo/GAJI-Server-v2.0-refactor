package gaji.service.domain.post.service;

import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.common.service.HashtagService;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.post.code.CommunityPostErrorStatus;
import gaji.service.domain.post.converter.CommunityPostConverter;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.repository.CommunityPostBookmarkRepository;
import gaji.service.domain.post.repository.CommunityPostJpaRepository;
import gaji.service.domain.post.repository.CommunityPostLikesRepository;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostQueryServiceImpl implements CommunityPostQueryService {
    private final CommunityPostJpaRepository communityPostJpaRepository;
    private final CommunityPostLikesRepository postLikesRepository;
    private final CommunityPostBookmarkRepository postBookmarkRepository;

    private final CategoryService categoryService;
    private final HashtagService hashtagService;
    private final CommunityPostBookMarkService postBookMarkService;
    private final CommunityPostLikesService postLikesService;



    @Override
    public CommunityPostResponseDTO.PostPreviewListDTO getPostList(String keyword,
                                   Integer lastPopularityScore,
                                   Long lastPostId,
                                   Integer lastLikeCnt,
                                   Integer lastHit,
                                   PostTypeEnum postType,
                                   String category,
                                   SortType sortType,
                                   PostStatusEnum postStatus,
                                   int page,
                                   int size) {
        PageRequest pageRequest = PageRequest.of(page, size);


        Long categoryId=null;
        if(category!=null)
        {
            categoryId=categoryService.findAllByCategory(CategoryEnum.fromValue(category)).get(0).getId();
        }

        Slice<CommnuityPost> postSlice = communityPostJpaRepository.findAllFetchJoinWithUser(keyword,
                lastPopularityScore,
                lastPostId,
                lastLikeCnt,
                lastHit,
                postType,
                postStatus,
                categoryId,
                sortType,
                pageRequest);

        List<CommunityPostResponseDTO.PostPreviewDTO> postPreviewDTOList = new ArrayList<>();

        for (CommnuityPost post : postSlice.getContent()) {
            List<SelectHashtag> selectHashtagList = hashtagService.findAllFetchJoinWithHashtagByEntityIdAndPostType(post.getId(), post.getType());
            postPreviewDTOList.add(CommunityPostConverter.toPostPreviewDTO(post, selectHashtagList));
        }

        return CommunityPostResponseDTO.PostPreviewListDTO.builder()
                .postList(postPreviewDTOList)
                .hasNext(postSlice.hasNext())
                .build();
    }

    @Override
    public CommunityPostResponseDTO.PostDetailDTO getPostDetail(Long userId, Long postId) {
        CommnuityPost findPost = communityPostJpaRepository.findByIdFetchJoinWithUser(postId);
        if (findPost == null) {
            throw new RestApiException(CommunityPostErrorStatus._POST_NOT_FOUND);
        }

        boolean isBookmarked = (userId == null) ? false : postBookMarkService.existsByUserAndPost(userId, findPost);
        boolean isLiked = (userId == null) ? false : postLikesService.existsByUserAndPost(userId, findPost);
        boolean isWriter = (userId == null) ? false : isPostWriter(userId, findPost);

        SelectCategory category = categoryService.findByEntityIdAndType(findPost.getId(), findPost.getType());

        List<SelectHashtag> selectHashtagList = hashtagService.findAllFetchJoinWithHashtagByEntityIdAndPostType(findPost.getId(), findPost.getType());

        findPost.increaseHitCnt();
        findPost.increasePopularityScoreByHit();
        return CommunityPostConverter.toPostDetailDTO(findPost, category, selectHashtagList, isBookmarked, isLiked, isWriter);
    }

    @Override
    public CommnuityPost findPostByPostId(Long postId) {
        return communityPostJpaRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(CommunityPostErrorStatus._POST_NOT_FOUND));
    }

    @Override
    public boolean isPostWriter(Long userId, CommnuityPost post) {
        return post.getUser().getId().equals(userId);
    }

    @Override
    public void validPostWriter(Long userId, CommnuityPost post) {
        if (!post.getUser().getId().equals(userId)) {
            throw new RestApiException(CommunityPostErrorStatus._NOT_AUTHORIZED);
        }
    }

    @Override
    public void validExistsPostLikes(Long userId, CommnuityPost post) {
        if (postLikesRepository.existsByUserIdAndPost(userId, post)) {
            throw new RestApiException(CommunityPostErrorStatus._ALREADY_EXIST_POST_LIKES);
        }
    }

    @Override
    public void validExistsPostBookmark(Long userId, CommnuityPost post) {
        if (postBookmarkRepository.existsByUserIdAndPost(userId, post)) {
            throw new RestApiException(CommunityPostErrorStatus._ALREADY_EXIST_POST_BOOKMARK);
        }
    }
}
