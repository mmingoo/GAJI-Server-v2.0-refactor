package gaji.service.domain.common.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HashtagResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HashtagNameAndIdDTO {
        private Long selectHashtagId;
        private String hashtagName;
    }
}
