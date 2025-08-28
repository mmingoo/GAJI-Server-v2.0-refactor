package gaji.service.domain.common.converter;

import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.web.dto.CategoryResponseDTO;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter implements Converter<String, CategoryEnum> {

    // @RequestParam으로 String->CategoryEnum으로 convert할 때 필요한 메서드
    @Override
    public CategoryEnum convert(String source) {
        CategoryEnum category = CategoryEnum.from(source);
        if (category == null) {
            throw new RestApiException(GlobalErrorStatus._INVALID_CATEGORY);
        }
        return category;
    }

    public static Category toCategory(CategoryEnum category) {
        return Category.builder()
                .category(category)
                .build();
    }

    public static SelectCategory toSelectCategory(Category category, Long entityId, PostTypeEnum type) {
        return SelectCategory.builder()
                        .category(category)
                        .entityId(entityId)
                        .type(type)
                        .build();
    }

    public static CategoryResponseDTO.BaseDTO toBaseDTO(Category category) {
        return CategoryResponseDTO.BaseDTO.builder()
                .categoryId(category.getId())
                .category(category.getCategory())
                .build();
    }

    public static List<CategoryResponseDTO.BaseDTO> toBaseDTOList(List<Category> categoryList) {
        return categoryList.stream()
                .map(CategoryConverter::toBaseDTO)
                .collect(Collectors.toList());
    }
}
