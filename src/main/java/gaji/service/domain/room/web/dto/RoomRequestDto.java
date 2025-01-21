package gaji.service.domain.room.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RoomRequestDto {
    @Schema(description = "과제 등록 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssignmentDto {

        @Schema(description = "과제 입력")
        @NotEmpty(message = "1개 이상의 과제를 입력해주세요.")
        private List<String> bodyList = new ArrayList<>();

    }

    @Schema(description = "과제 등록 DTO")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoomNoticeDto {
        @Schema(description = "제목")
        @NotNull(message = "제목을 입력해주세요.")
        private String title;

        @Schema(description = "내용")
        @NotNull(message = "내용을 입력해주세요.")
        private String body;
    }
    @Getter
    @Builder
    public static class StudyPeriodDto {
        @NotNull
        private LocalDate startDate;

        @NotNull
        private LocalDate endDate;
    }

    @Getter
    @Builder
    public static class StudyDescriptionDto {
        @NotBlank
        @Size(max = 30)
        private String title;

        @NotBlank
        @Size(max = 200)
        private String description;
    }

    @Getter
    @Setter
    public static class RoomEventUpdateDTO {
        private LocalDate startTime;
        private LocalDate endTime;
        private String description;
    }

    @Getter
    @Setter
    public static class AssignmentUpdateDTO {
        private String description;
    }


}
