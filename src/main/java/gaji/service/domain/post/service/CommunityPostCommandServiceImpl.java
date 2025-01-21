package gaji.service.domain.post.service;

import gaji.service.domain.common.converter.CategoryConverter;
import gaji.service.domain.common.converter.HashtagConverter;
import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.Hashtag;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.common.service.HashtagService;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.post.code.CommunityPostErrorStatus;
import gaji.service.domain.post.converter.CommunityCommentConverter;
import gaji.service.domain.post.converter.CommunityPostConverter;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.entity.PostBookmark;
import gaji.service.domain.post.entity.PostLikes;
import gaji.service.domain.post.repository.CommunityPostBookmarkRepository;
import gaji.service.domain.post.repository.CommunityPostJpaRepository;
import gaji.service.domain.post.repository.CommunityPostLikesRepository;
import gaji.service.domain.post.web.dto.CommunityPostCommentResponseDTO;
import gaji.service.domain.post.web.dto.CommunityPostRequestDTO;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityPostCommandServiceImpl implements CommunityPostCommandService {

    private final CommunityPostJpaRepository communityPostJpaRepository;
    private final CommunityPostBookmarkRepository postBookmarkRepository;
    private final CommunityPostLikesRepository postLikesRepository;
    private final UserQueryService userQueryService;
    private final CommunityPostQueryService communityPostQueryService;
    private final CommunityCommentService communityCommentService;
    private final HashtagService hashtagService;
    private final CategoryService categoryService;


    @Override
    public CommunityPostResponseDTO.PostIdResponseDTO uploadPost(Long userId, CommunityPostRequestDTO.UploadPostRequestDTO request) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost post = CommunityPostConverter.toPost(request, findUser);
        CommnuityPost newPost = communityPostJpaRepository.save(post);

        // 해시태그 저장
        // TODO: 해시태그 벌크성 insert 적용
        if (request.getHashtagList() != null) {
            List<String> hashtagStringList = request.getHashtagList();
            List<Hashtag> hashtagEntityList = hashtagService.createHashtagEntityList(hashtagStringList);

            List<SelectHashtag> selectHashtagList = HashtagConverter.toSelectHashtagList(hashtagEntityList, post.getId(), PostTypeEnum.from(request.getType()));
            hashtagService.saveAllSelectHashtag(selectHashtagList);
        }

        // SelectCategory 저장
        if (request.getCategory() != null) {
            String categoryValue = request.getCategory();
            //Category findCategry = categoryService.findByCategoryId(categoryId);
            Category findCategory = categoryService.findAllByCategory(CategoryEnum.fromValue(categoryValue)).get(0);

            SelectCategory selectCategory = CategoryConverter.toSelectCategory(findCategory, newPost.getId(), newPost.getType());
            categoryService.saveSelectCategory(selectCategory);
        }

        return CommunityPostConverter.toPostIdResponseDTO(newPost);
    }

    @Override
    public CommunityPostResponseDTO.PostIdResponseDTO editPost(Long userId, Long postId, CommunityPostRequestDTO.EditPostRequestDTO request) {
        // 조회
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 작성자 검증
        communityPostQueryService.validPostWriter(userId, findPost);

        return null;
    }

    @Override
    public CommunityPostCommentResponseDTO.WriteCommentResponseDTO writeCommentOnCommunityPost(Long userId, Long postId, Long parentCommentId, CommunityPostRequestDTO.WriteCommentRequestDTO request) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 저장
        CommunityComment newComment = communityCommentService.saveNewComment(
                communityCommentService.createCommentByCheckParentCommentIdIsNull(parentCommentId, request, findUser, findPost)
        );

        // 게시글의 댓글 수 증가
        newComment.getPost().increaseCommentCnt();
        return CommunityCommentConverter.toWriteCommentResponseDTO(newComment);
    }

    @Override
    public void hardDeleteComment(Long userId, Long commentId) {
        CommunityComment findComment = communityCommentService.findByCommentId(commentId);

        // 검증
        communityCommentService.validCommentWriter(userId, findComment);

        // 삭제
        communityCommentService.hardDeleteComment(findComment);

        // 게시글의 댓글 수 감소
        findComment.getPost().decreaseCommentCnt(); // TODO: 지연 로딩으로 쿼리 1개 더 날라감;
    }

    // TODO: 게시글 파일도 함께 삭제
    @Override
    public void hardDeleteCommunityPost(Long userId, Long postId) {
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 검증
        communityPostQueryService.validPostWriter(userId, findPost);

        // 삭제
        hashtagService.deleteAllByEntityIdAndType(findPost.getId(), findPost.getType());
        communityPostJpaRepository.delete(findPost);
    }

    @Override
    public CommunityPostResponseDTO.PostBookmarkIdDTO bookmarkCommunityPost(Long userId, Long postId) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 검증
        communityPostQueryService.validExistsPostBookmark(userId, findPost);

        // 저장
        PostBookmark newPostBookmark = postBookmarkRepository.save(CommunityPostConverter.toPostBookmark(findUser, findPost));

        // 게시글 북마크 수 증가
        newPostBookmark.getPost().increaseBookmarkCnt();
        return CommunityPostConverter.toPostBookmarkIdDTO(newPostBookmark);
    }

    @Override
    public void cancelbookmarkCommunityPost(Long userId, Long postId) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 삭제
        postBookmarkRepository.deleteByUserAndPost(findUser, findPost);

        // 게시글 북마크 수 감소
        findPost.decreaseBookmarkCnt();

        if (findPost.getBookmarkCnt() < 0) {
            throw new RestApiException(CommunityPostErrorStatus._BOOKMARK_CNT_NEGATIVE);
        }
    }

    @Override
    public CommunityPostResponseDTO.PostLikesIdDTO likeCommunityPost(Long userId, Long postId) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 검증
        communityPostQueryService.validExistsPostLikes(userId, findPost);

        // 저장
        PostLikes newPostLikes = postLikesRepository.save(CommunityPostConverter.toPostLikes(findUser, findPost));

        // 좋아요 수, 인기점수 증가
        findPost.increaseLikeCnt();
        findPost.increasePopularityScoreByLike();
        return CommunityPostConverter.toPostLikesIdDTO(newPostLikes);
    }

    @Override
    public void cancelLikeCommunityPost(Long userId, Long postId) {
        User findUser = userQueryService.findUserById(userId);
        CommnuityPost findPost = communityPostQueryService.findPostByPostId(postId);

        // 삭제
        postLikesRepository.deleteByUserAndPost(findUser, findPost);

        // 좋아요 수, 인기점수 감소
        findPost.decreaseLikeCnt();
        findPost.decreasePopularityScoreByLike();

        if (findPost.getBookmarkCnt() < 0) {
            throw new RestApiException(CommunityPostErrorStatus._LIKE_CNT_NEGATIVE);
        }

        if (findPost.getPopularityScore() < 0) {
            throw new RestApiException(CommunityPostErrorStatus._LIKE_CNT_NEGATIVE);
        }
    }
}
