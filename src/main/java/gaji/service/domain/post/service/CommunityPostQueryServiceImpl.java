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
import gaji.service.domain.post.entity.CommunityPost;
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

        Slice<CommunityPost> postSlice = communityPostJpaRepository.findAllFetchJoinWithUser(keyword,
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

        for (CommunityPost post : postSlice.getContent()) {
            List<SelectHashtag> selectHashtagList = hashtagService.findAllFetchJoinWithHashtagByEntityIdAndPostType(post.getId(), post.getType());
            postPreviewDTOList.add(CommunityPostConverter.toPostPreviewDTO(post, selectHashtagList));
        }

        return CommunityPostResponseDTO.PostPreviewListDTO.builder()
                .postList(postPreviewDTOList)
                .hasNext(postSlice.hasNext())
                .build();
    }

    @Override
    @Transactional
    public CommunityPostResponseDTO.PostDetailDTO getPostDetail(Long userId, Long postId) {
        CommunityPost findPost = communityPostJpaRepository.findByIdFetchJoinWithUser(postId);
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
    public CommunityPost findPostByPostId(Long postId) {
        return communityPostJpaRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(CommunityPostErrorStatus._POST_NOT_FOUND));
    }

    @Override
    public boolean isPostWriter(Long userId, CommunityPost post) {
        return post.getUser().getId().equals(userId);
    }

    @Override
    public void validPostWriter(Long userId, CommunityPost post) {
        if (!post.getUser().getId().equals(userId)) {
            throw new RestApiException(CommunityPostErrorStatus._NOT_AUTHORIZED);
        }
    }

    @Override
    public void validExistsPostLikes(Long userId, CommunityPost post) {
        if (postLikesRepository.existsByUserIdAndPost(userId, post)) {
            throw new RestApiException(CommunityPostErrorStatus._ALREADY_EXIST_POST_LIKES);
        }
    }

    @Override
    public void validExistsPostBookmark(Long userId, CommunityPost post) {
        if (postBookmarkRepository.existsByUserIdAndPost(userId, post)) {
            throw new RestApiException(CommunityPostErrorStatus._ALREADY_EXIST_POST_BOOKMARK);
        }
    }

    @Override
    @Transactional
    public String putPostStatus(Long userId, Long postId) {
        // 게시글 조회
        CommunityPost post = findPostByPostId(postId);

        // 요청한 사용자와 게시글 작성자 비교
        if (!post.getUser().getId().equals(userId)) {
            throw new RestApiException(CommunityPostErrorStatus._NOT_AUTHORIZED);
        }

        // 현재 상태에 따라 자동으로 변경될 상태 결정
        PostStatusEnum newStatus = determineNextStatus(post);

        // 상태 변경
        post.updateStatus(newStatus);

        return post.getStatus().getValue();
    }

    private PostStatusEnum determineNextStatus(CommunityPost post) {
        PostTypeEnum type = post.getType();
        PostStatusEnum currentStatus = post.getStatus();

        if (type == PostTypeEnum.PROJECT) {
            if (currentStatus == PostStatusEnum.RECRUITING) {
                return PostStatusEnum.RECRUITMENT_COMPLETED;
            } else if (currentStatus == PostStatusEnum.RECRUITMENT_COMPLETED) {
                return PostStatusEnum.RECRUITING;
            }
        } else if (type == PostTypeEnum.QUESTION) {
            if (currentStatus == PostStatusEnum.NEED_RESOLUTION) {
                return PostStatusEnum.RESOLVED;
            } else if (currentStatus == PostStatusEnum.RESOLVED) {
                return PostStatusEnum.NEED_RESOLUTION;
            }
        }

        throw new RestApiException(CommunityPostErrorStatus._INVALID_POST_STATUS);
    }



}
