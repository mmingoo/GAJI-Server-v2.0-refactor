package gaji.service.domain.recruit.converter;

import gaji.service.domain.enums.CategoryEnum;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CategoryConverter implements Converter<String, CategoryEnum> {

    @Override
    public CategoryEnum convert(String param) {
        if (!StringUtils.hasText(param)) throw new RestApiException(GlobalErrorStatus._INVALID_CATEGORY);
        return CategoryEnum.from(param);
    }
}