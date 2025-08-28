package gaji.service.domain.post.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostCommentResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriteCommentResponseDTO {
        private Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCommentDTO {
        private Long commentId;
        private Long userId;
        private String userNickName;
        private String commentBody;
        private Integer groupNum;
        private int depth;
        private String commentWriteDate;
        private boolean isWriter;
        private String profileImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCommentListDTO {
        private List<PostCommentDTO> commentList = new ArrayList<>();
        private boolean hasNext;
    }
}
