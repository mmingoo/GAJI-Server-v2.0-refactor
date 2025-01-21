package gaji.service.domain.common.service;

import gaji.service.domain.common.converter.CategoryConverter;
import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.repository.CategoryRepository;
import gaji.service.domain.common.repository.SelectCategoryRepository;
import gaji.service.domain.common.web.dto.CategoryResponseDTO;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SelectCategoryRepository selectCategoryRepository;

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public SelectCategory saveSelectCategory(SelectCategory selectCategory) {
        return selectCategoryRepository.save(selectCategory);
    }

    @Override
    public Category findByCategory(CategoryEnum category) {
        return categoryRepository.findByCategory(category);
    }

    //todo: 나중에 카테고리 DB에 통일 하면 지워야함
    @Override
    public List<Category> findAllByCategory(CategoryEnum category) {
        return categoryRepository.findAllByCategory(category);
    }

    @Override
    public boolean existsByEntityIdAndType(Long entityId, PostTypeEnum type) {
        return selectCategoryRepository.existsByEntityIdAndType(entityId, type);
    }

    @Override
    public Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._INVALID_CATEGORY));
    }

    @Override
    public List<Long> findEntityIdListByCategoryIdAndPostType(Long categoryId, PostTypeEnum postType) {
        return selectCategoryRepository.findEntityIdListByCategoryAndPostType(findByCategoryId(categoryId), postType);
    }

    @Override
    public boolean existsByCategory(CategoryEnum category) {
        return categoryRepository.existsByCategory(category);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public SelectCategory findOneFetchJoinWithCategoryByEntityIdAndPostType(Long entityId, PostTypeEnum postType) {
        return selectCategoryRepository.findOneFetchJoinWithCategoryByEntityIdAndPostType(entityId, postType);
    }

    @Override
    @Transactional
    public List<CategoryResponseDTO.BaseDTO> findAllCategory() {
        return CategoryConverter.toBaseDTOList(categoryRepository.findAll());
    }

    @Override
    @Transactional
    public void saveAllSelectCategory(List<SelectCategory> selectCategoryList) {
        selectCategoryRepository.saveAll(selectCategoryList);
    }

    @Override
    public SelectCategory findByEntityIdAndType(Long entityId, PostTypeEnum type) {
        return selectCategoryRepository.findByEntityIdAndType(entityId, type);
    }

    @Override
    public void deleteByEntityIdAndType(Long entityId, PostTypeEnum type) {
        selectCategoryRepository.deleteByEntityIdAndType(entityId, type);
    }
}
