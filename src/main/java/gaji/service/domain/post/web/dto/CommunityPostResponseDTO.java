package gaji.service.domain.post.web.dto;

import gaji.service.domain.common.web.dto.HashtagResponseDTO;
import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


public class CommunityPostResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostIdResponseDTO {
        Long postId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostBookmarkIdDTO {
        Long postBookmarkId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostLikesIdDTO {
        Long postLikesId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO {
        private Long postId;
        private int likeCnt;
        private String thumbnailUrl;
        private String title;
        private String body;
        private Long userId;
        private String userNickname;
        private String uploadTime;
        private int hit;
        private int popularityScore;
        private PostStatusEnum status;
        private List<String> hashtagList = new ArrayList<>();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewListDTO {
        private List<PostPreviewDTO> postList = new ArrayList<>();
        private boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDetailDTO {
        private Long userId;
        private PostTypeEnum type;
        private String createdAt;
        private int hit;
        private int likeCnt;
        private int bookmarkCnt;
        private int commentCnt;
        private String userNickname;
        private String title;
        private boolean isWriter; // 게시글을 조회한 사람이 작성자가 맞는지
        private boolean bookMarkStatus;
        private boolean likeStatus;
        private String body;
        private String category;
        private PostStatusEnum status;
        private List<HashtagResponseDTO.HashtagNameAndIdDTO> hashtagList = new ArrayList<>();
    }


}
