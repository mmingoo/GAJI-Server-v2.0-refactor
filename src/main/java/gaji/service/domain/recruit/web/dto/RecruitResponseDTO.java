package gaji.service.domain.recruit.web.dto;

import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.RecruitPostTypeEnum;
import gaji.service.domain.enums.UserActive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecruitResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomResponseDTO {
        Long roomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRoomResponseDTO {
        Long roomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class studyDetailResponseDTO {
        // 유저 관련
        Long writerId;
        String userNickName;
        UserActive userActive;
        LocalDateTime inactiveTime;

        String studyTitle;
        String imageUrl;
        RecruitPostTypeEnum recruitPostTypeEnum;
        CategoryEnum studyCategory;
        int views;
        int likeCnt;
        int bookmarkCnt;
        Boolean likeStatus;
        Boolean bookmarkStatus;

        LocalDate recruitStartTime;
        LocalDate recruitEndTime;
        LocalDate studyStartTime;
        LocalDate studyEndTime;
        List<String> materialList;
        String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponseDTO {
        String profileImageUrl;
        String userNickName;
        Integer commentOrder;
        int depth;
        Long commentId;
        String commentWriteDate;
        String commentBody;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListResponseDTO {
        int commentCount;
        boolean hasNext;
        List<CommentResponseDTO> commentList;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyLikesIdResponseDTO {
        Long studyLikesId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyBookmarkIdDTO {
        Long studyBookmarkId;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriteCommentResponseDTO {
        Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentResponseDTO {
        Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewResponseDTO {
        Long roomId;
        String imageUrl;
        RecruitPostTypeEnum recruitStatus;
        int applicant;
        String name;
        Long deadLine;
        String description;
        String createdAt;
        int recruitMaxCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewListResponseDTO {
        List<PreviewResponseDTO> previewList;
        boolean hasNext;
        Long lastValue;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultPreviewDTO {
        CategoryEnum category;
        boolean hasNext;
        List<PreviewResponseDTO> previewList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultPreviewListResponseDTO {
        List<DefaultPreviewDTO> defaultPreviewList;
        boolean hasNext;
        int nextCategoryId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinStudyResponseDTO {
        Long roomId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitCompleteResponseDTO {
        Long roomId;
    }
}
