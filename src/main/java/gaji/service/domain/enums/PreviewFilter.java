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
public enum PreviewFilter {
    RECRUITING("모집중"),
    RECRUITMENT_COMPLETED("모집 완료"),
    PEOPLE_LIMITED("인원 제한"),
    PEOPLE_NOT_LIMITED("인원 제한 없음");

    @JsonValue
    private final String value;

    @JsonCreator // Json -> Object, 역직렬화 수행하는 메서드
    public static PreviewFilter from(String param) {
        for (PreviewFilter filter : PreviewFilter.values()) {
            if (filter.getValue().equals(param)) {
                return filter;
            }
        }
        log.error("Filter.from() exception occur param: {}", param);
        throw new RestApiException(GlobalErrorStatus._FILTER_NOT_VALID);
    }
}

