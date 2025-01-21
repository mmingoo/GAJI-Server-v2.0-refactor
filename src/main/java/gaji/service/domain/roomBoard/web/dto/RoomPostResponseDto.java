package gaji.service.domain.roomBoard.web.dto;

import gaji.service.global.converter.DateConverter;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class RoomPostResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomPostDTO {
        Long postId;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class MainPostSummaryDto {
        private Long id;
        private String title;
        private String body;
        private String userNickname;
        private LocalDateTime createdAt;
        private int viewCount;
        private String timeSincePosted;

        public MainPostSummaryDto(Long id, String title, String body, String userNickname, LocalDateTime createdAt, int viewCount) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.userNickname = userNickname;
            this.createdAt = createdAt;
            this.viewCount = viewCount;
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostListDto {
        private Long postId;
        private String title;
        private String content;
        private Integer viewCount;
        private LocalDateTime postTime;
        private Long userId;
        private String profileImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class toCreateRoomTroublePostIdDTO {
        Long troublePostId;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class toWriteCommentDto {
        Long commentId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class toCreateRoomInfoPostIdDTO {
        Long infoPostId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class toCreateRoomPostIdDTO {
        Long roomPostId;
    }


    @Getter
    public static class TroublePostSummaryDto {
        private final Long id;
        private final String title;
        private final String nickname;
        private final String createdAt;
        private final int viewCount;
        private final int commentCount;

        public TroublePostSummaryDto(Long id, String title, String nickname, LocalDateTime createdAt, int viewCount, int commentCount) {
            this.id = id;
            this.title = title;
            this.nickname = nickname;
            this.createdAt = DateConverter.convertToRelativeTimeFormat(createdAt);
            this.viewCount = viewCount;
            this.commentCount = commentCount;
        }
    }

    @Getter
    @Setter
    public static class PostSummaryDto {
        private final Long id;
        private final String title;
        private final String nickname;
        private final String createdAt;
        private final int viewCount;
        private final int commentCount;

        public PostSummaryDto(Long id, String title, String nickname, LocalDateTime createdAt, int viewCount, int commentCount) {
            this.id = id;
            this.title = title;
            this.nickname = nickname;
            this.createdAt = DateConverter.convertToRelativeTimeFormat(createdAt);
            this.viewCount = viewCount;
            this.commentCount = commentCount;
        }
    }

    @Getter
    public static class InfoPostSummaryDto {
        private final Long id;
        private final String title;
        private final String nickname;
        private final String createdAt;
        private final int viewCount;
        private final int commentCount;

        public InfoPostSummaryDto(Long id, String title, String nickname, LocalDateTime createdAt, int viewCount, int commentCount) {
            this.id = id;
            this.title = title;
            this.nickname = nickname;
            this.createdAt = DateConverter.convertToRelativeTimeFormat(createdAt);
            this.viewCount = viewCount;
            this.commentCount = commentCount;
        }
    }

    @Getter
    @Setter
    public static class TroublePostDetailDTO {
        private Long id;
        private String title;
        private String body;
        private String authorName;
        private LocalDateTime createdAt;
        private int viewCount;
        private int likeCount;
        private int bookmarkCount;
        private boolean isLiked;
        private boolean isBookmarked;
        private Page<CommentWithRepliesDTO> comments;
    }

    @Getter
    @Setter
    public static class CommentWithRepliesDTO {
        private Long id;
        private String userNickName;
        private String commentBody;
        private LocalDateTime commentWriteDate;
        private List<CommentDTO> replies;
    }

    @Getter
    @Setter
    public static class CommentDTO {
        private Long id;
        private String userNickName;
        private String commentBody;
        private LocalDateTime commentWriteDate;
    }

    @Getter
    @Setter
    public static class RoomInfoPostDetailDTO {
        private Long id;
        private String title;
        private String body;
        private String authorName;
        private LocalDateTime createdAt;
        private int viewCount;
        private int likeCount;
        private int bookmarkCount;
        private boolean isLiked;
        private boolean isBookmarked;
        private Page<CommentWithRepliesDTO> comments;
    }

    @Getter
    @Setter
    public static class RoomPostDetailDTO {
        private Long id;
        private String title;
        private String body;
        private String authorName;
        private LocalDateTime createdAt;
        private int viewCount;
        private int likeCount;
        private int bookmarkCount;
        private boolean isLiked;
        private boolean isBookmarked;
        private Page<CommentWithRepliesDTO> comments;
    }
}







