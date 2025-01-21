package gaji.service.domain.post.converter;

import gaji.service.domain.enums.SortType;
import org.springframework.core.convert.converter.Converter;

public class SortTypeConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String param) {
        return SortType.from(param);
    }
}
