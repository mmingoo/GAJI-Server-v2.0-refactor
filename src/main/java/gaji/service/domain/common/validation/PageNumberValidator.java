package gaji.service.domain.common.validation;

import gaji.service.domain.common.annotation.CheckPage;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PageNumberValidator implements ConstraintValidator<CheckPage, Integer> {

    @Override
    public void initialize(CheckPage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        boolean isValid = value>=0;

        if (!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GlobalErrorStatus._INTERNAL_PAGE_ERROR.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}