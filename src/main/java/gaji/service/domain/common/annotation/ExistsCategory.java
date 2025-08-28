package gaji.service.domain.common.annotation;

import gaji.service.domain.common.validation.CategoryExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsCategory {
    String message() default "올바른 카테고리 값을 입력해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
