package gaji.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum CategoryEnum {
    DEVELOP("개발"),
    AI("인공지능"),
    HW("하드웨어"),
    SECURITY("보안"),
    NETWORK("네트워크-클라우드"),
    LANGUAGE("어학"),
    DESIGN("디자인"),
    BUSINESS("비즈니스"),
    BOOK("독서"),
    ETC("기타");

    @JsonValue
    private final String value;

    CategoryEnum(String value) {
        this.value = value;
    }

    @JsonCreator // Json -> Object, 역직렬화 수행하는 메서드
    public static CategoryEnum from(String param) {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getValue().equals(param)) {
                return categoryEnum;
            }
        }
        log.error("CategoryEnum.from() exception occur param: {}", param);
        return null;
    }

    public static CategoryEnum fromValue(String value) {
        for (CategoryEnum category : CategoryEnum.values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }
}