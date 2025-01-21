package gaji.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import gaji.service.domain.post.code.CommunityPostErrorStatus;
import gaji.service.global.exception.RestApiException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum PostStatusEnum {
    RECRUITING("모집중"),
    RECRUITMENT_COMPLETED("모집완료"),
    NEED_RESOLUTION("미완료질문"),
    RESOLVED("해결완료"),
    DELETED("삭제됨"),
    BLOGING("블로그"),
    TEMPORARY("임시저장");

    @JsonValue
    private final String value;

    PostStatusEnum(String value) {
        this.value = value;
    }

    @JsonCreator // Json -> Object, 역직렬화 수행하는 메서드
    public static PostStatusEnum from(String param) {
        for (PostStatusEnum postStatusEnum : PostStatusEnum.values()) {
            if (postStatusEnum.getValue().equals(param)) {
                return postStatusEnum;
            }
        }
        log.error("PostStatusEnum.from() exception occur param: {}", param);
        throw new RestApiException(CommunityPostErrorStatus._INVALID_POST_STATUS);
    }
}
