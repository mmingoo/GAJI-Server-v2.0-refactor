package gaji.service.domain.recruit.service;

import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.common.service.CategoryService;
import gaji.service.domain.common.web.dto.CategoryResponseDTO;
import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.PreviewFilter;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.recruit.code.RecruitErrorStatus;
import gaji.service.domain.recruit.converter.RecruitConverter;
import gaji.service.domain.recruit.repository.RecruitRepository;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.service.RoomCommandService;
import gaji.service.domain.room.service.RoomQueryService;
import gaji.service.domain.user.entity.User;
import gaji.service.domain.user.service.UserQueryService;
import gaji.service.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitQueryServiceImpl implements RecruitQueryService {

    private final UserQueryService userQueryService;
    private final RoomQueryService roomQueryService;
    private final RoomCommandService roomCommandService;
    private final CategoryService categoryService;
    private final RecruitRepository recruitRepository;
    private final RecruitCommandService recruitCommandService;

    @Override
    @Transactional
    public RecruitResponseDTO.studyDetailResponseDTO getStudyDetail(Long roomId) {

        Room room = roomQueryService.findRoomById(roomId);
        User user = userQueryService.findUserById(room.getUser().getId());
        boolean likeStatus = recruitCommandService.userLikeStatus(room, user);
        boolean bookmarkStatus = recruitCommandService.userBookmarkStatus(room, user);

        room.addView();
        roomCommandService.saveRoom(room);

        CategoryEnum category;
        if (categoryService.existsByEntityIdAndType(roomId, PostTypeEnum.ROOM)) {
            SelectCategory selectCategory =
                    categoryService.findByEntityIdAndType(room.getId(), PostTypeEnum.ROOM);

            category = RecruitConverter.toCategory(selectCategory);
        } else {
            category = null;
        }


        return RecruitConverter.toStudyDetailDTO(user, room, category, likeStatus, bookmarkStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public RecruitResponseDTO.PreviewListResponseDTO getPreviewList(
            String categoryValue, PreviewFilter filter, SortType sort, String query, Long value, int pageSize) {

        CategoryEnum category = null;
        if (categoryValue != null) {
            category = CategoryEnum.fromValue(categoryValue);
        }

        validateQuery(query);

        Pageable pageable = PageRequest.of(0, pageSize);

        return recruitRepository.findByCategoryOrderBySortType(category, filter, sort, query, value, pageable);
    }

    @Override
    public RecruitResponseDTO.DefaultPreviewListResponseDTO getDefaultPreview(boolean isFirst, int nextCategoryId, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<RecruitResponseDTO.DefaultPreviewDTO> defaultPreviewList = new ArrayList<>();
        List<CategoryResponseDTO.BaseDTO> categoryList = categoryService.findAllCategory();

        boolean hasNext = true;
        int count = 0;
        int getPreviewCount;

        if (isFirst) {
            nextCategoryId = 0;
            getPreviewCount = 4;
        } else {
            nextCategoryId--;
            getPreviewCount = 1;
        }

        while (count < getPreviewCount) {
            if (categoryList.size() <= nextCategoryId) {
                hasNext = false;
                break;
            }

            CategoryEnum category = categoryList.get(nextCategoryId++).getCategory();

            RecruitResponseDTO.DefaultPreviewDTO previewList =
                    recruitRepository.findByCategory(category, pageable);

            if (previewList.getPreviewList() == null || previewList.getPreviewList().isEmpty()) {
                continue;
            }
            count++;
            defaultPreviewList.add(previewList);
        }

        if (hasNext) {
            RecruitResponseDTO.DefaultPreviewDTO previewList =
                    recruitRepository.findByCategory(categoryList.get(nextCategoryId).getCategory(), PageRequest.of(0, 1));

            if (previewList.getPreviewList().isEmpty()) {
                hasNext = false;
                nextCategoryId = -1;
            }
        }


        return RecruitResponseDTO.DefaultPreviewListResponseDTO.builder()
                .defaultPreviewList(defaultPreviewList)
                .nextCategoryId(++nextCategoryId)
                .hasNext(hasNext)
                .build();
    }

    private void validateQuery(String query) {
        if (query == null) {
            return; // query가 null이면 검사를 하지 않음.
        }

        int queryLength = query.length();

        if (queryLength < 2) {
            throw new RestApiException(RecruitErrorStatus._QUERY_SO_SHORT);
        }
        if (queryLength > 20) {
            throw new RestApiException(RecruitErrorStatus._QUERY_SO_LONG);
        }
    }
}
