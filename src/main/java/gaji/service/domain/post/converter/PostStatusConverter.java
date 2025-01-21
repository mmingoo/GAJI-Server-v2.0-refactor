package gaji.service.domain.post.converter;

import gaji.service.domain.enums.PostStatusEnum;
import org.springframework.core.convert.converter.Converter;

public class PostStatusConverter implements Converter<String, PostStatusEnum> {

    @Override
    public PostStatusEnum convert(String param) {
        return PostStatusEnum.from(param);
    }
}
