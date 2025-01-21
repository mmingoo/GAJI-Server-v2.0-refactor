package gaji.service.domain.common.service;

import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.web.dto.CategoryResponseDTO;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;

import java.util.List;

public interface CategoryService {
    Category saveCategory(Category category);
    SelectCategory saveSelectCategory(SelectCategory selectCategory);
    Category findByCategory(CategoryEnum category);
    Category findByCategoryId(Long categoryId);
    List<Long> findEntityIdListByCategoryIdAndPostType(Long categoryId, PostTypeEnum postType);
    boolean existsByCategory(CategoryEnum category);
    boolean existsByCategoryId(Long categoryId);
    SelectCategory findOneFetchJoinWithCategoryByEntityIdAndPostType(Long entityId, PostTypeEnum postType);
    List<CategoryResponseDTO.BaseDTO> findAllCategory();
    void saveAllSelectCategory(List<SelectCategory> selectCategoryList);

    SelectCategory findByEntityIdAndType(Long entityId, PostTypeEnum type);

    void deleteByEntityIdAndType(Long entityId, PostTypeEnum type);

    List<Category> findAllByCategory(CategoryEnum category);

    boolean existsByEntityIdAndType(Long entityId, PostTypeEnum type);
}

