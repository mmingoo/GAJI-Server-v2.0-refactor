package gaji.service.domain.roomBoard.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public class RoomPostRequestDto {

    @Schema(description = "스터디 게시글 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class RoomPostDto {
        @Schema(description = "게시글 제목")
        @NotBlank(message = "제목을 입력해주세요.")
        private final String title;

        @Schema(description = "게시글 본문")
        @NotBlank(message = "게시글 본문을 입력해주세요.")
        private final String body;

    }

    @Schema(description = "스터디 게시글 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class RoomTroubloePostDto {
        @Schema(description = "게시글 제목")
        @NotBlank(message = "제목을 입력해주세요.")
        private final String title;

        @Schema(description = "게시글 본문")
        @NotBlank(message = "게시글 본문을 입력해주세요.")
        private final String body;
    }

    @Schema(description = " 게시글 댓글 작성 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class RoomTroubleCommentDto {
        @Schema(description = "댓글 본문")
        @NotBlank(message = "댓글 본문을 입력해주세요.")
        private String body;
    }

    @Schema(description = "스터디 게시글 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class RoomInfoPostDto {
        @Schema(description = "게시글 제목")
        @NotBlank(message = "제목을 입력해주세요.")
        private final String title;

        @Schema(description = "게시글 본문")
        @NotBlank(message = "게시글 본문을 입력해주세요.")
        private final String body;

    }

  }

