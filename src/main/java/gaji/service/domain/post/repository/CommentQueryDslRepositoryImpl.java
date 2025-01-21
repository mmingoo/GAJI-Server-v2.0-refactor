package gaji.service.domain.post.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.CommunityComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gaji.service.domain.post.entity.QCommunityComment.communityComment;
import static gaji.service.domain.user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class CommentQueryDslRepositoryImpl implements CommentQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<CommunityComment> findBySliceAndPostFetchJoinWithUser(Integer lastGroupNum, CommnuityPost post, Pageable pageable) {
        List<CommunityComment> commentList = jpaQueryFactory
                .select(communityComment)
                .from(communityComment)
                .leftJoin(communityComment.user, user)
                .fetchJoin()
                .where(
                        communityComment.post.eq(post),
                        gtGroupNum(lastGroupNum)
                )
                .groupBy(
                        communityComment.groupNum,
                        communityComment.id,
                        communityComment.body,
                        communityComment.depth,
                        communityComment.status,
                        communityComment.user,
                        communityComment.createdAt
                )
                .orderBy(
                        orderByGroupNumAndDepth()
                )
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, commentList);
    }

    // no-offest 페이징 처리
    private BooleanExpression gtGroupNum(Integer lastGroupNum) {
        return (lastGroupNum != null) ? communityComment.groupNum.gt(lastGroupNum) : null;
    }

    private Slice<CommunityComment> checkLastPage(Pageable pageable, List<CommunityComment> commentList) {
        boolean hasNext = false;

        // (조회한 결과 개수 > 요청한 페이지 사이즈) 이면 뒤에 데이터가 더 존재함
        if (commentList.size() > pageable.getPageSize()) {
            hasNext = true;
            commentList.remove(pageable.getPageSize()); // limit(pageable.getPageSize() + 1) 로 1개 더 가져온 데이터를 삭제해줌.
        }

        return new SliceImpl<>(commentList, pageable, hasNext);
    }

    private OrderSpecifier[] orderByGroupNumAndDepth() {
        return new OrderSpecifier[] {
                communityComment.groupNum.asc(),
                communityComment.depth.asc()
        };
    }
}
