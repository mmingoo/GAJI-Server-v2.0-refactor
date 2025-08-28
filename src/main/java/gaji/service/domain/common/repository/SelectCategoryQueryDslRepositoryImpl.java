package gaji.service.domain.common.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.enums.PostTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gaji.service.domain.common.entity.QCategory.category1;
import static gaji.service.domain.common.entity.QSelectCategory.selectCategory;


@Repository
@RequiredArgsConstructor
public class SelectCategoryQueryDslRepositoryImpl implements SelectCategoryQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SelectCategory findOneFetchJoinWithCategoryByEntityIdAndPostType(Long entityId, PostTypeEnum postType) {
        return jpaQueryFactory
                .selectFrom(selectCategory)
                .join(selectCategory.category, category1)
                .fetchJoin()
                .where(
                        selectCategory.entityId.eq(entityId),
                        selectCategory.type.eq(postType)
                )
                .fetchOne()
                ;
    }

    @Override
    public List<Long> findEntityIdListByCategoryAndPostType(Category category, PostTypeEnum postType) {
        return jpaQueryFactory
                .select(selectCategory.entityId)
                .from(selectCategory)
                .where(
                        categoryEq(category),  // Category 조건
                        postTypeEq(postType)       // PostTypeEnum 조건
                )
                .fetch(); // 결과를 List<Long>으로 반환
    }

    private BooleanExpression postTypeEq(PostTypeEnum postTypeCond) {
        return (postTypeCond != null) ? selectCategory.type.eq(postTypeCond) : null;
    }

    private BooleanExpression categoryEq(Category categoryCond) {
        return (categoryCond != null) ? selectCategory.category.eq(categoryCond) : null;
    }
}
