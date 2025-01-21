package gaji.service.domain.post.converter;

import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.post.code.CommunityPostErrorStatus;
import gaji.service.global.exception.RestApiException;
import org.springframework.core.convert.converter.Converter;

public class PostTypeConverter implements Converter<String, PostTypeEnum> {

    @Override
    public PostTypeEnum convert(String param) {
        PostTypeEnum postType = PostTypeEnum.from(param);
        if (postType == null) {
            throw new RestApiException(CommunityPostErrorStatus._INVALID_POST_TYPE);
        }
        return postType;
    }
}
