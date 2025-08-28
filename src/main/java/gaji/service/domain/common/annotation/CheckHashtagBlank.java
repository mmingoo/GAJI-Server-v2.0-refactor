package gaji.service.domain.common.annotation;

import gaji.service.domain.common.validation.HashtagBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HashtagBlankValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckHashtagBlank {
    String message() default "올바른 해시태그 값을 입력해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
