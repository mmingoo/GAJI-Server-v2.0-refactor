package gaji.service.domain.user.validation;

import gaji.service.domain.user.annotation.ExistUser;
import gaji.service.domain.user.code.UserErrorStatus;
import gaji.service.domain.user.service.UserQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistValidator implements ConstraintValidator<ExistUser, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(ExistUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        boolean isExist = userQueryService.existUserById(value); // user가 존재하면 1 아니면 0

        if (!isExist) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(UserErrorStatus._USER_NOT_FOUND.getMessage()).addConstraintViolation();
        }

        return isExist;
    }
}
