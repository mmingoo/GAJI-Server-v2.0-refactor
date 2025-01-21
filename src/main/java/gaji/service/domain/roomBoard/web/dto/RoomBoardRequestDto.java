package gaji.service.domain.roomBoard.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class RoomBoardRequestDto {
    @Schema(description = "게시글 댓글 DTO")
    @Getter
    @RequiredArgsConstructor
    public static class TroubleCommnetDto {
        @Schema(description = "게시글 댓글")
        @NotBlank(message ="댓글을 입력해주세요.")
        private final String body;

    }
}
