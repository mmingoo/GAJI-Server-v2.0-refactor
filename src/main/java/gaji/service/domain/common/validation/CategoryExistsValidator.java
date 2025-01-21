package gaji.service.domain.common.validation;

import gaji.service.domain.common.annotation.ExistsCategory;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CategoryExistsValidator implements ConstraintValidator<ExistsCategory, String> {
    private final CategoryService categoryService;

    @Override
    public void initialize(ExistsCategory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid;

        if (value == null || value.isBlank()) {
            return true;
        }

        isValid = categoryService.existsByCategory(CategoryEnum.from(value));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GlobalErrorStatus._INVALID_CATEGORY.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}
