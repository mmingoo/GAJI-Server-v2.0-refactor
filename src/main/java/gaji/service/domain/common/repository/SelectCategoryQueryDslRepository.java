package gaji.service.domain.common.repository;

import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.enums.PostTypeEnum;

import java.util.List;

public interface SelectCategoryQueryDslRepository {
    SelectCategory findOneFetchJoinWithCategoryByEntityIdAndPostType(Long entityId, PostTypeEnum postType);
    List<Long> findEntityIdListByCategoryAndPostType(Category category, PostTypeEnum postType);
}
