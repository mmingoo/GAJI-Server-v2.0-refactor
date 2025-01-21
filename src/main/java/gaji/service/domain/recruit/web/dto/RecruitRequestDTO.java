package gaji.service.domain.recruit.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import gaji.service.domain.common.annotation.ExistsCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class RecruitRequestDTO {

    @Schema(description = "스터디 생성 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class RoomContentDTO {

        @Schema(description = "스터디 이름")
        @Size(max = 20, message = "스터디 명은 20자 이내로 입력해주세요.")
        private String name;

        @Schema(description = "스터디 상세 내용")
        @Size(max = 20000, message = "스터디 상세 내용은 20000자 이내로 입력해주세요.")
        private String description;

        @Schema(description = "썸네일 경로")
        private String thumbnailUrl;

        @Schema(description = "스터디 자료들")
        private List<String> materialList;

        @Schema(description = "스터디 모집 시작일")
        @NotNull(message = "스터디 모집 기한을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate recruitStartDay;

        @Schema(description = "스터디 모집 종료일")
        @NotNull(message = "스터디 모집 기한을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate recruitEndDay;

        @Schema(description = "스터디 모집 시작일")
        @NotNull(message = "스터디 모집 기한을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate studyStartDay;

        @Schema(description = "스터디 모집 종료일")
        @NotNull(message = "스터디 모집 기한을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate studyEndDay;

        // true 시 초대코드 생성
        @Schema(description = "공개 여부")
        @JsonProperty("private")
        @NotNull(message = "스터디 공개 여부를 선택해주세요.")
        private boolean isPrivate;

        //인원제한 여부 Ture : 제한있음 / False : 제한없음
        @Schema(description = "인원 제한 여부")
        @NotNull(message = "인원 제한 여부를 선택해주세요.")
        private boolean peopleLimited;

        //인원제한 True 일 시 입력(false 면 0) -> 인원 채워지면 모집 완료로 변경
        @Schema(description = "최대 인원")
        @Min(value = 1, message = "최대 인원은 1이상 이어야 합니다.")
        private int peopleMaximum;

        @Schema(description = "카테고리")
        @ExistsCategory
        private String category;
    }

    @Schema(description = "스터디 댓글 작성 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class CommentContentDTO {
        @Schema(description = "댓글 내용")
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        private String body;
    }
}
