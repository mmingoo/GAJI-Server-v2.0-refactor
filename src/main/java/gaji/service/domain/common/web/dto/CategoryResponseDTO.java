package gaji.service.domain.common.web.dto;

import gaji.service.domain.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseDTO {
        private Long categoryId;
        private CategoryEnum category;
    }
}
