package gaji.service.domain.post.validation;

import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.post.annotation.ExistPostType;
import gaji.service.domain.post.code.CommunityPostErrorStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PostTypeExistValidator implements ConstraintValidator<ExistPostType, String> {

    @Override
    public void initialize(ExistPostType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = PostTypeEnum.from(value) != null;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CommunityPostErrorStatus._INVALID_POST_TYPE.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}
