package gaji.service.domain.recruit.converter;

import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.Role;
import gaji.service.domain.recruit.entity.RecruitPostBookmark;
import gaji.service.domain.recruit.entity.RecruitPostLikes;
import gaji.service.domain.recruit.entity.StudyComment;
import gaji.service.domain.recruit.web.dto.RecruitRequestDTO;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Material;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.user.entity.User;
import gaji.service.global.converter.DateConverter;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecruitConverter {

    public static Room toRoom(RecruitRequestDTO.RoomContentDTO request, User user, String thumbnailUrl, String inviteCode, int peopleMaximum) {
        return Room.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailUrl(thumbnailUrl)
                .recruitStartDay(request.getRecruitStartDay())
                .recruitEndDay(request.getRecruitEndDay())
                .studyStartDay(request.getStudyStartDay())
                .studyEndDay((request.getStudyEndDay()))
                .isPrivate(request.isPrivate())
                .inviteCode(inviteCode)
                .peopleLimited(request.isPeopleLimited())
                .peopleMaximum(peopleMaximum)
                .build();
    }

    public static RecruitResponseDTO.CreateRoomResponseDTO toCreateRoomResponseDTO(Room room) {
        return RecruitResponseDTO.CreateRoomResponseDTO.builder()
                .roomId(room.getId())
                .build();
    }

    public static RecruitResponseDTO.UpdateRoomResponseDTO toUpdateRoomResponseDTO(Room room) {
        return RecruitResponseDTO.UpdateRoomResponseDTO.builder()
                .roomId(room.getId())
                .build();
    }

    public static CategoryEnum toCategory(SelectCategory selectCategory) {
        return selectCategory.getCategory().getCategory();
    }

    public static Material toMaterial(String materialPath, Room room) {
        return Material.builder()
                .room(room)
                .path(materialPath)
                .build();
    }

    public static StudyMate toStudyMate(User user, Room room, Role role) {
        return StudyMate.builder()
                .user(user)
                .room(room)
                .role(role)
                .build();
    }

    public static RecruitResponseDTO.studyDetailResponseDTO toStudyDetailDTO(User user, Room room, CategoryEnum category, boolean likeStatus, boolean bookmarkStatus) {
        return RecruitResponseDTO.studyDetailResponseDTO.builder()
                .userNickName(user.getNickname())
                .userActive(user.getStatus())
                .inactiveTime(user.getInactiveTime())
                .writerId(user.getId())

                .studyTitle(room.getName())
                .imageUrl(room.getThumbnailUrl())
                .recruitPostTypeEnum(room.getRecruitPostTypeEnum())
                .studyCategory(category)
                .views(room.getViews())
                .likeCnt(room.getLikes())
                .bookmarkCnt(room.getBookmarks())
                .likeStatus(likeStatus)
                .bookmarkStatus(bookmarkStatus)

                .recruitStartTime(room.getRecruitStartDay())
                .recruitEndTime(room.getRecruitEndDay())
                .studyStartTime(room.getStudyStartDay())
                .studyEndTime(room.getStudyEndDay())
                .materialList(room.getMaterialList().stream().map(Material::getPath).collect(Collectors.toList()))
                .description(room.getDescription())
                .build();
    }

    private static RecruitResponseDTO.CommentResponseDTO toCommentResponseDTO(StudyComment comment) {
        return RecruitResponseDTO.CommentResponseDTO.builder()
                .profileImageUrl(comment.getUser().getProfileImagePth())
                .userNickName(comment.getUser().getNickname())
                .commentOrder(comment.getCommentOrder())
                .depth(comment.getDepth())
                .commentId(comment.getId())
                .commentWriteDate(DateConverter.convertToRelativeTimeFormat(comment.getCreatedAt())+" 작성")
                .commentBody(comment.getBody())
                .build();
    }

    public static RecruitResponseDTO.CommentListResponseDTO toCommentListDTO(int commentCount, Slice<StudyComment> commentList) {
        List<RecruitResponseDTO.CommentResponseDTO> CommentResponseDTO =
                commentList.stream().map(RecruitConverter::toCommentResponseDTO).collect(Collectors.toList());

        return RecruitResponseDTO.CommentListResponseDTO.builder()
                .commentCount(commentCount)
                .hasNext(commentList.hasNext())
                .commentList(CommentResponseDTO)
                .build();
    }

    public static StudyComment toComment(RecruitRequestDTO.CommentContentDTO request, User user, Room room, StudyComment parentComment) {
        return StudyComment.builder()
                .user(user)
                .room(room)
                .parentComment(parentComment)
                .body(request.getBody())
                .build();
    }

    public static RecruitResponseDTO.WriteCommentResponseDTO toWriteCommentDTO(StudyComment comment) {
        return RecruitResponseDTO.WriteCommentResponseDTO.builder()
                .commentId(comment.getId())
                .build();
    }

    public static RecruitResponseDTO.UpdateCommentResponseDTO toUpdateCommentDTO(StudyComment comment) {
        return RecruitResponseDTO.UpdateCommentResponseDTO.builder()
                .commentId(comment.getId())
                .build();
    }

    public static RecruitPostLikes toRecruitPostLikes(User user, Room room) {
        return RecruitPostLikes.builder()
                .user(user)
                .room(room)
                .build();
    }

    public static RecruitResponseDTO.StudyLikesIdResponseDTO toStudyLikesIdDTO(RecruitPostLikes likes) {
        return RecruitResponseDTO.StudyLikesIdResponseDTO.builder()
                .studyLikesId(likes.getId())
                .build();
    }

    public static RecruitPostBookmark toRecruitPostBookmark(User user, Room room) {
        return RecruitPostBookmark.builder()
                .user(user)
                .room(room)
                .build();
    }

    public static RecruitResponseDTO.StudyBookmarkIdDTO toStudyBookmarkIdDTO(RecruitPostBookmark bookmark) {
        return RecruitResponseDTO.StudyBookmarkIdDTO.builder()
                .studyBookmarkId(bookmark.getId())
                .build();
    }

    public static RecruitResponseDTO.PreviewResponseDTO toPreviewDTO(Room room) {
        return RecruitResponseDTO.PreviewResponseDTO.builder()
                .roomId(room.getId())
                .imageUrl(room.getThumbnailUrl())
                .recruitStatus(room.getRecruitPostTypeEnum())
                .applicant(room.getStudyMateList().size())
                .name(room.getName())
                .deadLine(ChronoUnit.DAYS.between(LocalDate.now(), room.getRecruitEndDay()))
                .description(room.getDescription())
                .createdAt(DateConverter.convertToRelativeTimeFormat(room.getCreatedAt()))
                .recruitMaxCount(room.getPeopleMaximum())
                .build();
    }

    public static List<RecruitResponseDTO.PreviewResponseDTO> toPreviewDTOLIST(List<Room> roomList) {
        return roomList.stream().map(RecruitConverter::toPreviewDTO).collect(Collectors.toList());
    }

    public static RecruitResponseDTO.JoinStudyResponseDTO toJoinStudyResponseDTO(Long roomId) {
        return RecruitResponseDTO.JoinStudyResponseDTO.builder()
                .roomId(roomId)
                .build();
    }

    public static RecruitResponseDTO.RecruitCompleteResponseDTO toRecruitCompleteResponseDTO(Long roomId) {
        return RecruitResponseDTO.RecruitCompleteResponseDTO.builder()
                .roomId(roomId)
                .build();
    }
}
