package gaji.service.domain.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static gaji.service.domain.common.entity.QSelectCategory.selectCategory;
import static gaji.service.domain.common.entity.QSelectHashtag.selectHashtag;
import static gaji.service.domain.post.entity.QCommnuityPost.commnuityPost;
import static gaji.service.domain.user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class CommunityPostQueryDslRepositoryImpl implements CommunityPostQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final CategoryService categoryService;

    @Override
    public Slice<CommnuityPost> findAllFetchJoinWithUser(String keyword,
                                                Integer lastPopularityScore,
                                                Long lastPostId,
                                                Integer lastLikeCnt,
                                                Integer lastHit,
                                                PostTypeEnum postType,
                                                PostStatusEnum postStatus,
                                                Long categoryId,
                                                SortType sortType,
                                                Pageable pageable) {
        List<Long> entityIdList = (categoryId != null) ? getEntityIdListByCategoryIdAndPostType(categoryId, postType) : null;

        List<CommnuityPost> postList = jpaQueryFactory.
                selectFrom(commnuityPost)
                .leftJoin(commnuityPost.user, user)
                .join(selectCategory)
                .on(joinSelectCategory(commnuityPost.id, commnuityPost.type))
//                .join(selectHashtag) // TODO: 연관관계가 맺어져 있지 않아도 조인 가능, 추후 리팩토링 고려
//                .on(joinSelectHashtag(commnuityPost.id, commnuityPost.type))
                .fetchJoin()
                .where(
                        ltPopularityScore(lastPopularityScore),
                        ltPostId(lastPostId),
                        ltLikeCnt(lastLikeCnt),
                        ltHit(lastHit),
                        postTypeEq(postType),
                        postStatusEq(postStatus),
                        postIdIn(entityIdList),
                        searchByKeyword(keyword)
                )
                .orderBy(orderBySortType(sortType))
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, postList);
    }

    private List<Long> getEntityIdListByCategoryIdAndPostType(Long categoryId, PostTypeEnum postType) {
        return categoryService.findEntityIdListByCategoryIdAndPostType(categoryId, postType);
    }

    @Override
    public CommnuityPost findByIdFetchJoinWithUser(Long postId) {
        return jpaQueryFactory.selectFrom(commnuityPost)
                .leftJoin(commnuityPost.user, user)
                .where(
                        postIdEq(postId)
                )
                .fetchOne();
    }

    @Override
    public Slice<Tuple> findAllPostsByUser(User user, LocalDateTime cursorDateTime, Pageable pageable, PostTypeEnum type) {
        List<Tuple> userPosts = jpaQueryFactory.select(commnuityPost.id, commnuityPost.user, commnuityPost.title, commnuityPost.body, commnuityPost.type, commnuityPost.status, commnuityPost.hit, commnuityPost.likeCnt, commnuityPost.createdAt)
                .from(commnuityPost)
                .where(commnuityPost.user.eq(user), (postTypeEq(type))
                        ,(commnuityPost.createdAt.before(cursorDateTime))
                )
                .orderBy(commnuityPost.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, userPosts);
    }

    private BooleanExpression postIdEq(Long postId) {
        return commnuityPost.id.eq(postId);
    }

    private BooleanExpression postTypeEq(PostTypeEnum postTypeCond) {
        return (postTypeCond != null) ? commnuityPost.type.eq(postTypeCond) : null;
    }

    private BooleanExpression postStatusEq(PostStatusEnum postStatusCond) {
        return (postStatusCond != null) ? commnuityPost.status.eq(postStatusCond) : null;
    }

    private BooleanExpression ltPopularityScore(Integer popularityScore) {
        return (popularityScore != null) ? commnuityPost.popularityScore.lt(popularityScore) : null;
    }

    private BooleanExpression ltPostId(Long lastPostId) {
        return (lastPostId != null) ? commnuityPost.id.lt(lastPostId) : null;
    }

    private BooleanExpression ltLikeCnt(Integer lastLikeCnt) {
        return (lastLikeCnt != null) ? commnuityPost.likeCnt.lt(lastLikeCnt) : null;
    }

    private BooleanExpression postIdIn(List<Long> postIdList) {
        return (postIdList != null) ? commnuityPost.id.in(postIdList) : null;
    }

    private BooleanExpression ltHit(Integer lastHit) {
        return (lastHit != null) ? commnuityPost.hit.lt(lastHit) : null;
    }

    private BooleanExpression searchByKeyword(String keyword) {
        return (keyword != null) ? commnuityPost.title.containsIgnoreCase(keyword).or(commnuityPost.body.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression joinSelectHashtag(NumberPath<Long> entityId, EnumPath<PostTypeEnum> postType) {
        return selectHashtag.entityId.eq(entityId)
                .and(selectHashtag.type.eq(postType));
    }

    private BooleanExpression joinSelectCategory(NumberPath<Long> entityId, EnumPath<PostTypeEnum> postType) {
        return selectCategory.entityId.eq(entityId)
                .and(selectCategory.type.eq(postType));
    }

    private <T> Slice<T> checkLastPage(Pageable pageable, List<T> postList) {
        boolean hasNext = false;

        // (조회한 결과 개수 > 요청한 페이지 사이즈) 이면 뒤에 데이터가 더 존재함
        if (postList.size() > pageable.getPageSize()) {
            hasNext = true;
            postList.remove(pageable.getPageSize()); // limit(pageable.getPageSize() + 1) 로 1개 더 가져온 데이터를 삭제해줌.
        }
        return new SliceImpl<>(postList, pageable, hasNext);
    }

    private OrderSpecifier orderBySortType(SortType sortTypeCond) {
        return switch (sortTypeCond) {
            case HOT -> commnuityPost.popularityScore.desc(); // HOT: 인기점수 내림차순
            case LIKE -> commnuityPost.likeCnt.desc(); // LIKE: 좋아요 내림차순
            case HIT -> commnuityPost.hit.desc(); // HIT: 조회수 내림차순
            default -> commnuityPost.createdAt.desc(); // null or RECENT: 최신순(생성일자 내림차순)
        };
    }
}
