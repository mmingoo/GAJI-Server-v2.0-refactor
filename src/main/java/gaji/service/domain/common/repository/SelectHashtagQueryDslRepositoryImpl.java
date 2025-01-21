package gaji.service.domain.common.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.enums.PostTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gaji.service.domain.common.entity.QHashtag.hashtag;
import static gaji.service.domain.common.entity.QSelectHashtag.selectHashtag;


@Repository
@RequiredArgsConstructor
public class SelectHashtagQueryDslRepositoryImpl implements SelectHashtagQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SelectHashtag> findAllFetchJoinWithHashtagByEntityIdAndPostType(Long entityId, PostTypeEnum postType) {
        return jpaQueryFactory.selectFrom(selectHashtag)
                .join(selectHashtag.hashtag, hashtag).fetchJoin()
                .where(
                        selectHashtag.entityId.eq(entityId),
                        selectHashtag.type.eq(postType)
                )
                .orderBy(selectHashtag.id.asc())
                .fetch();
    }
}
