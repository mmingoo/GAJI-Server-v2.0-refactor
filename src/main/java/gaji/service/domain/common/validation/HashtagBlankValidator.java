package gaji.service.domain.common.validation;

import gaji.service.domain.common.annotation.CheckHashtagBlank;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HashtagBlankValidator implements ConstraintValidator<CheckHashtagBlank, List<String>> {

    @Override
    public void initialize(CheckHashtagBlank constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        // 리스트 자체가 null인 경우 유효성 검사에서 제외
        if (values == null) {
            return true;
        }

        // 유효성 검사 통과 조건
        // 리스트 안의 모든 element가 ["", " ", null]값에 해당되지 않아야함
        boolean isValid = values.stream()
                .allMatch(value -> value != null && !value.trim().isEmpty());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GlobalErrorStatus._HASHTAG_ISBLANK.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}
