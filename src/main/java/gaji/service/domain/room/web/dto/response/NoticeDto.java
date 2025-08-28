package gaji.service.domain.room.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    private Long id;
    private String authorName;
    private String title;
    private String body;
    private Long confirmCount;
    private LocalDateTime createdAt;
    private Integer viewCount;
    private String timeSincePosted;

    public NoticeDto(Long id, String authorName, String title, String body, Long confirmCount, LocalDateTime createdAt, Integer viewCount) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.body = body;
        this.confirmCount = confirmCount;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public void setTimeSincePosted(String timeSincePosted) {
        this.timeSincePosted = timeSincePosted;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
