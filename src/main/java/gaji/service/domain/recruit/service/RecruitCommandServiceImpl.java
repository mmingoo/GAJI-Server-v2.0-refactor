package gaji.service.domain.recruit.service;

import gaji.service.domain.common.converter.CategoryConverter;
import gaji.service.domain.common.entity.Category;
import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.RecruitPostTypeEnum;
import gaji.service.domain.enums.Role;
import gaji.service.domain.recruit.code.RecruitErrorStatus;
import gaji.service.domain.recruit.converter.RecruitConverter;
import gaji.service.domain.recruit.entity.RecruitPostBookmark;
import gaji.service.domain.recruit.entity.RecruitPostLikes;
import gaji.service.domain.recruit.repository.RecruitPostBookmarkRepository;
import gaji.service.domain.recruit.repository.RecruitPostLikesRepository;
import gaji.service.domain.recruit.web.dto.RecruitRequestDTO;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Material;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.MaterialCommandService;
import gaji.service.domain.room.service.RoomCommandService;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.studyMate.code.StudyMateErrorStatus;
import gaji.service.domain.studyMate.entity.StudyMate;
import gaji.service.domain.studyMate.service.StudyMateCommandService;
import gaji.service.domain.studyMate.service.StudyMateQueryService;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService {

    private final RoomCommandService roomCommandService;
    private final UserQueryService userQueryService;
    private final CategoryService categoryService;
    private final RoomQueryService roomQueryService;
    private final StudyMateCommandService studyMateCommandService;
    private final StudyMateQueryService studyMateQueryService;
    private final MaterialCommandService materialCommandService;
    private final RecruitPostLikesRepository recruitPostLikesRepository;
    private final RecruitPostBookmarkRepository recruitPostBookmarkRepository;
    private final StudyCommentCommandService studyCommentCommandService;

    @Override
    @Transactional
    public RecruitResponseDTO.CreateRoomResponseDTO createRoom(RecruitRequestDTO.RoomContentDTO request, Long userId) {

        String inviteCode = null;
        int peopleMaximum = 0;

        if (request.isPrivate()) {
            inviteCode = generateInviteCode();
        }

        if (request.isPeopleLimited()) {
            peopleMaximum = request.getPeopleMaximum();
        }

        User user = userQueryService.findUserById(userId);
        Room room = RecruitConverter.toRoom(request, user, request.getThumbnailUrl(), inviteCode, peopleMaximum);

        StudyMate studyMate = RecruitConverter.toStudyMate(user, room, Role.READER);
        studyMateCommandService.saveStudyMate(studyMate);

        addMaterial(request.getMaterialList(), room);
        roomCommandService.saveRoom(room);

        addCategory(request.getCategory(), room);

        return RecruitConverter.toCreateRoomResponseDTO(room);
    }

    @Override
    @Transactional
    public RecruitResponseDTO.UpdateRoomResponseDTO updateRoom(RecruitRequestDTO.RoomContentDTO request, Long userId, Long roomId) {
        Room room = roomQueryService.findRoomById(roomId);

        if (!room.getUser().getId().equals(userId)) {
            throw new RestApiException(StudyMateErrorStatus._ONLY_LEADER_POSSIBLE);
        }

        String inviteCode = null;
        int peopleMaximum = 0;

        if (request.isPrivate()) {
            inviteCode = generateInviteCode();
        }

        if (request.isPeopleLimited()) {
            peopleMaximum = request.getPeopleMaximum();
        }

        room.update(request, request.getThumbnailUrl(), inviteCode, peopleMaximum);
        ;

        materialCommandService.deleteAllByRoom(room);
        addMaterial(request.getMaterialList(), room);

        roomCommandService.saveRoom(room);

        if (categoryService.existsByEntityIdAndType(roomId, PostTypeEnum.ROOM)) {
            SelectCategory existCategory = categoryService.findByEntityIdAndType(roomId, PostTypeEnum.ROOM);
            if (!existCategory.getCategory().getCategory().getValue().equals(request.getCategory())) {
                categoryService.deleteByEntityIdAndType(roomId, PostTypeEnum.ROOM);
                addCategory(request.getCategory(), room);
            }
        } else {
            addCategory(request.getCategory(), room);
        } 

        return RecruitConverter.toUpdateRoomResponseDTO(room);
    }

    private void addCategory(String category, Room room) {
        if (category != null && !category.isBlank()) {
            Category newCategory = categoryService.findByCategory(CategoryEnum.fromValue(category));
            SelectCategory selectCategory = CategoryConverter.toSelectCategory(newCategory, room.getId(), PostTypeEnum.ROOM);
            categoryService.saveSelectCategory(selectCategory);
        }
    }

    private void addMaterial(List<String> materialList, Room room) {
        if (materialList != null && !materialList.isEmpty()){
            Material material;
            for (String MaterialUrl : materialList) {
                material = RecruitConverter.toMaterial(MaterialUrl, room);
                room.addMaterial(material);
                materialCommandService.saveMaterial(material);
            }
        }
    }

    private String generateInviteCode() {
        int CODE_LENGTH = 6;
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        Random random = new SecureRandom();
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARS.length());
            code.append(CHARS.charAt(index));
        }

        return code.toString();
    }

    @Override
    @Transactional
    public void deleteStudy(Long userId, Long roomId) {
        Room room = roomQueryService.findRoomById(roomId);

        if (!room.getUser().getId().equals(userId)) {
            throw new RestApiException(StudyMateErrorStatus._ONLY_LEADER_POSSIBLE);
        }

        studyCommentCommandService.deleteByRoom(room);
        categoryService.deleteByEntityIdAndType(roomId, PostTypeEnum.ROOM);

        roomCommandService.deleteRoom(room);
    }

    @Override
    @Transactional
    public RecruitResponseDTO.StudyLikesIdResponseDTO likeStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (recruitPostLikesRepository.existsByUserAndRoom(user, room)) {
            throw new RestApiException(RecruitErrorStatus._ROOM_ALREADY_LIKE);
        }

        RecruitPostLikes studyLikes = recruitPostLikesRepository.save(RecruitConverter.toRecruitPostLikes(user, room));
        room.increaseLike();

        return RecruitConverter.toStudyLikesIdDTO(studyLikes);
    }

    @Override
    @Transactional
    public void unLikeStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (!recruitPostLikesRepository.existsByUserAndRoom(user, room)) {
            throw new RestApiException(RecruitErrorStatus._ROOM_ALREADY_NO_LIKE);
        }

        recruitPostLikesRepository.deleteByUserAndRoom(user, room);
        room.decreaseLike();
    }

    @Override
    @Transactional
    public RecruitResponseDTO.StudyBookmarkIdDTO bookmarkStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (recruitPostBookmarkRepository.existsByUserAndRoom(user, room)) {
            throw new RestApiException(RecruitErrorStatus._ROOM_ALREADY_BOOKMARK);
        }

        RecruitPostBookmark studyBookmark = recruitPostBookmarkRepository.save(RecruitConverter.toRecruitPostBookmark(user, room));
        room.increaseBookmark();

        return RecruitConverter.toStudyBookmarkIdDTO(studyBookmark);
    }

    @Override
    @Transactional
    public void unBookmarkStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (!recruitPostBookmarkRepository.existsByUserAndRoom(user, room)) {
            throw new RestApiException(RecruitErrorStatus._ROOM_ALREADY_NO_BOOKMARK);
        }
        recruitPostBookmarkRepository.deleteByUserAndRoom(user, room);
        room.decreaseBookmark();
    }

    @Override
    @Transactional
    public RecruitResponseDTO.JoinStudyResponseDTO joinStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (studyMateQueryService.existsByUserAndRoom(user, room)) {
            throw new RestApiException(StudyMateErrorStatus._USER_ALREADY_JOIN);
        }

        StudyMate studyMate = RecruitConverter.toStudyMate(user, room, Role.MEMBER);
        studyMateCommandService.saveStudyMate(studyMate);

        return RecruitConverter.toJoinStudyResponseDTO(roomId);
    }

    @Override
    @Transactional
    public void leaveStudy(Long userId, Long roomId) {
        User user = userQueryService.findUserById(userId);
        Room room = roomQueryService.findRoomById(roomId);

        if (!studyMateQueryService.existsByUserAndRoom(user, room)) {
            throw new RestApiException(StudyMateErrorStatus._USER_NOT_IN_STUDYROOM);
        } else {
            if (studyMateQueryService.checkLeader(user, room)) {
                throw new RestApiException(StudyMateErrorStatus._LEADER_IMPOSSIBLE_LEAVE);
            }
            studyMateCommandService.deleteByUserAndRoom(user, room);
        }
    }

    @Override
    @Transactional
    public void kickStudy(Long userId, Long roomId, Long targetId) {
        Room room = roomQueryService.findRoomById(roomId);
        User target = userQueryService.findUserById(targetId);

        StudyMate studyMate = studyMateQueryService.findByUserIdAndRoomId(userId, roomId);
        if (studyMate.getRole() != Role.READER) {
            throw new RestApiException(StudyMateErrorStatus._ONLY_LEADER_POSSIBLE);
        }

        if (!studyMateQueryService.existsByUserAndRoom(target, room)) {
            throw new RestApiException(StudyMateErrorStatus._USER_NOT_IN_STUDYROOM);
        } else {
            if (studyMateQueryService.checkLeader(target, room)) {
                throw new RestApiException(StudyMateErrorStatus._LEADER_IMPOSSIBLE_LEAVE);
            }
            studyMateCommandService.deleteByUserAndRoom(target, room);
        }
    }

    @Override
    @Transactional
    public RecruitResponseDTO.RecruitCompleteResponseDTO recruitComplete(Long userId, Long roomId) {
        Room room = roomQueryService.findRoomById(roomId);

        if (room.getRecruitPostTypeEnum().equals(RecruitPostTypeEnum.RECRUITMENT_COMPLETED)) {
            throw new RestApiException(RecruitErrorStatus._RECRUIT_POST_ALREADY_COMPLETE);
        }

        if (!room.getUser().getId().equals(userId)) {
            throw new RestApiException(StudyMateErrorStatus._ONLY_LEADER_POSSIBLE);
        }

        room.updateRecruitStatus(RecruitPostTypeEnum.RECRUITMENT_COMPLETED);
        roomCommandService.saveRoom(room);

        return RecruitConverter.toRecruitCompleteResponseDTO(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userLikeStatus(Room room, User user) {
        return recruitPostLikesRepository.existsByUserAndRoom(user, room);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userBookmarkStatus(Room room, User user){
        return recruitPostBookmarkRepository.existsByUserAndRoom(user, room);
    }
}
