package gaji.service.domain.recruit.converter;

import gaji.service.domain.enums.PreviewFilter;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FilterConverter implements Converter<String, PreviewFilter> {

    @Override
    public PreviewFilter convert(String param) {
        if (!StringUtils.hasText(param)) throw new RestApiException(GlobalErrorStatus._FILTER_NOT_VALID);
        return PreviewFilter.from(param);
    }
}