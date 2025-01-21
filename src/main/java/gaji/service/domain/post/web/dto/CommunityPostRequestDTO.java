package gaji.service.domain.post.web.dto;

import gaji.service.domain.common.annotation.CheckHashtagBlank;
import gaji.service.domain.common.annotation.CheckHashtagLength;
import gaji.service.domain.common.annotation.ExistsCategory;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.post.annotation.ExistPostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostRequestDTO {

    @Schema(description = "커뮤니티 게시글 저장 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class UploadPostRequestDTO {
        @Schema(description = "게시글 제목")
        @NotBlank(message = "게시글 제목을 입력해주세요.")
        private final String title;

        @Schema(description = "게시글 본문")
        @NotBlank(message = "게시글 본문을 입력해주세요.")
        private final String body;

        @Schema(description = "게시글 썸네일 Url")
        private final String thumbnailUrl;

        @Schema(description = "게시글 유형(프로젝트 모집, 질문, 블로그)")
        @ExistPostType
        private final String type;

        @Schema(description = "해시태그 리스트")
        @CheckHashtagBlank
        @CheckHashtagLength
        private final List<String> hashtagList = new ArrayList<>();

        @Schema(description = "카테고리")
        @ExistsCategory
        private final String category;
    }

    @Schema(description = "커뮤니티 게시글 수정 DTO")
    @Getter
    public static class EditPostRequestDTO {
        @Schema(description = "게시글 제목")
        private String title;

        @Schema(description = "게시글 본문")
        private String body;

        @Schema(description = "카테고리")
        @ExistsCategory
        private String category;

        @Schema(description = "해시태그 리스트")
        @CheckHashtagBlank
        @CheckHashtagLength
        private final List<String> hashtagList = new ArrayList<>();
    }

    @Schema(description = "커뮤니티 게시글 댓글 작성 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class WriteCommentRequestDTO {
        @Schema(description = "댓글 본문")
        @NotBlank(message = "댓글 본문을 입력해주세요.")
        private String body;
    }

}
