package gaji.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public enum SortType {

    HOT("hot"),
    RECENT("recent"),
    LIKE("like"),
    HIT("hit")
    ; // 인기순, 최신순, 좋아요순, 히트순

    @JsonValue
    private final String value;

    @JsonCreator // Json -> Object, 역직렬화 수행하는 메서드
    public static SortType from(String param) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getValue().equals(param)) {
                return sortType;
            }
        }
        log.error("SortType.from() exception occur param: {}", param);
        throw new RestApiException(GlobalErrorStatus._SORT_TYPE_NOT_VALID);
    }
}
