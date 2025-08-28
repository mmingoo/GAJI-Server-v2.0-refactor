package gaji.service.domain.user.web.dto;

import gaji.service.domain.enums.PostStatusEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.UserActive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserIdDTO {
        Long userId;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancleResultDTO {
        Long userId;
        UserActive userActive;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNicknameResultDTO {
        Long userId;
        String nickname;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRoomDTO {
        Long roomId;
        String name;
        String description;
        String thumbnail_url;
        LocalDate studyStartDay;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRoomListDTO {
        List<GetRoomDTO> roomList;
        boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostDTO {
        Long postId;
        Long userId;
        String title;
        String body;
        PostTypeEnum type;
        PostStatusEnum status;
        String nickname;
        String profileImagePth;
        String createdAt;
        int viewCnt;
        int likeCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostListDTO {
        List<GetPostDTO> postList;
        boolean hasNext;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetUserDetailDTO {
        Long userId;
        String nickname;
        String profileImagePth;
    }
}



