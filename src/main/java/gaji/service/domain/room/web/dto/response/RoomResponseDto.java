package gaji.service.domain.room.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import gaji.service.domain.room.entity.RoomEvent;
import gaji.service.domain.studyMate.entity.Assignment;
import gaji.service.domain.studyMate.entity.WeeklyUserProgress;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class RoomResponseDto {

    @Getter
    @Setter
    @Builder
    public static class AssignmentResponseDto {
        private List<Long> assignmentIds;
        private LocalDate deadline;
        private Long daysLeft;

        public static AssignmentResponseDto of(List<Assignment> assignments, RoomEvent roomEvent) {
            List<Long> ids = assignments.stream()
                    .map(Assignment::getId)
                    .collect(Collectors.toList());

            return AssignmentResponseDto.builder()
                    .assignmentIds(ids)
                    .deadline(roomEvent.getEndTime())
                    .daysLeft(calculateDaysLeft(roomEvent.getEndTime()))
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class AssignmentProgressResponse {
        @JsonProperty("progressPercentage")
        private Double progressPercentage;

        @JsonProperty("completedAssignments")
        private Integer completedAssignments;

        @JsonProperty("totalAssignments")
        private Integer totalAssignments;

        @JsonProperty("isCompleted")
        private Boolean isCompleted;

        @JsonProperty("deadline")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate deadline;


        @JsonProperty("daysLeft")
        private Long daysLeft;

        public static AssignmentProgressResponse of(WeeklyUserProgress progress, RoomEvent roomEvent) {
            return AssignmentProgressResponse.builder()
                    .progressPercentage(progress.getProgressPercentage())
                    .completedAssignments(progress.getCompletedAssignments())
                    .totalAssignments(progress.getTotalAssignments())
                    .isCompleted(progress.getProgressPercentage() >= 100.0)
                    .deadline(roomEvent.getEndTime())
                    .daysLeft(calculateDaysLeft(roomEvent.getEndTime()))
                    .build();
        }
    }

    public static long calculateDaysLeft(LocalDate deadline) {
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }


    @Getter
    @Builder
    public static class EventResponseDto {
        private Long eventId;

        public static EventResponseDto of(Long eventId) {
            return EventResponseDto.builder()
                    .eventId(eventId)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignmentDto {
        Long id;
        Integer weeks;
        String body;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomNoticeDto {
        Long noticeId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    public static class RoomMainDto {
        private String name;
        private LocalDate startDay;
        private LocalDate endDay;
        private LocalDate recruitStartDay;
        private LocalDate recruitEndDay;
        private Long daysLeftForRecruit;
        private Long applicantCount;

        public RoomMainDto(String name, LocalDate startDay, LocalDate endDay,
                           LocalDate recruitStartDay, LocalDate recruitEndDay,
                           Long daysLeftForRecruit, Long applicantCount) {
            this.name = name;
            this.startDay = startDay;
            this.endDay = endDay;
            this.recruitStartDay = recruitStartDay;
            this.recruitEndDay = recruitEndDay;
            this.applicantCount = applicantCount;
            this.daysLeftForRecruit = Math.max(daysLeftForRecruit, 0L);
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainRoomNoticeDto {
        private Long latestNoticeId;
        private String latestNoticeTitle;
        private List<NoticePreview> noticePreviews;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class NoticePreview {
            private Long id;
            private String title;
            private String body;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NoticeDtoList {
        private List<NoticeDto> noticeDtoList;
    }

    @Getter
    @Builder
    public static class UserProgressDTO {
        private String nickname;
        private Long userId;
        private Double progressPercentage;
    }

    @Getter
    @Builder
    public static class WeeklyStudyInfoDTO {
        private Integer weekNumber;
        private StudyPeriodDTO studyPeriod;
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class StudyPeriodDTO {
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomMainNoticeDto {
        private Long id;
        private String authorName;
        private String title;
        private String body;
        private Long confirmCount;
        private LocalDateTime createdAt;
        private Integer viewCount;
        private String timeSincePosted;

        public RoomMainNoticeDto(Long id, String authorName, String title, String body, Long confirmCount, LocalDateTime createdAt, Integer viewCount) {
            this.id = id;
            this.authorName = authorName;
            this.title = title;
            this.body = body;
            this.confirmCount = confirmCount;
            this.createdAt = createdAt;
            this.viewCount = viewCount;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class IsConfirmedResponse {
        private Boolean isConfirmed;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @RequiredArgsConstructor
    public static class roomEventIdDto {
        private Long roomEventId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @RequiredArgsConstructor
    public static class AssignmentIdDto {
        private Long assignmentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @RequiredArgsConstructor
    public static class NoticeIdDto {
        private Long noticeId;
    }
}