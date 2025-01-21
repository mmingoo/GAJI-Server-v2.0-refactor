package gaji.service.domain.recruit.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.enums.*;
import gaji.service.domain.recruit.converter.RecruitConverter;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import gaji.service.domain.room.entity.Room;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gaji.service.domain.common.entity.QSelectCategory.selectCategory;
import static gaji.service.domain.room.entity.QRoom.room;

@RequiredArgsConstructor
@Repository
public class RecruitCustomRepositoryImpl implements RecruitCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public RecruitResponseDTO.PreviewListResponseDTO findByCategoryOrderBySortType(
            CategoryEnum category, PreviewFilter filter, SortType sortType, String query, Long value, Pageable pageable) {
        List<Room> results = queryFactory
                .selectFrom(room)
                .leftJoin(selectCategory).on(selectCategory.entityId.eq(room.id)
                        .and(selectCategory.type.eq(PostTypeEnum.ROOM)))
                .where(categoryEq(category), lastStudyValue(sortType, value), checkFilter(filter), searchPost(query))
                .orderBy(getOrderSpecifier(sortType))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = checkLastPage(pageable, results);

        List<RecruitResponseDTO.PreviewResponseDTO> previewList = RecruitConverter.toPreviewDTOLIST(results);

        return RecruitResponseDTO.PreviewListResponseDTO.builder()
                .previewList(previewList)
                .lastValue(getLastValue(results, sortType))
                .hasNext(hasNext)
                .build();
    }

    @Override
    public RecruitResponseDTO.DefaultPreviewDTO findByCategory(CategoryEnum category, Pageable pageable) {
        List<Room> results = queryFactory
                .selectFrom(room)
                .join(selectCategory).on(selectCategory.entityId.eq(room.id)
                        .and(selectCategory.type.eq(PostTypeEnum.ROOM))
                        .and(categoryEq(category)))
                .orderBy(room.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = checkLastPage(pageable, results);

        List<RecruitResponseDTO.PreviewResponseDTO> previewList = RecruitConverter.toPreviewDTOLIST(results);

        return RecruitResponseDTO.DefaultPreviewDTO.builder()
                .previewList(previewList)
                .category(category)
                .hasNext(hasNext)
                .build();
    }

    private boolean checkLastPage(Pageable pageable, List<Room> roomList) {
        boolean hasNext = roomList.size() > pageable.getPageSize();
        if (hasNext) {
            roomList.remove(roomList.size() - 1);
        }
        return hasNext;
    }

    private BooleanExpression categoryEq(CategoryEnum category) {
        return category != null ? selectCategory.category.category.eq(category) : null;
    }

    private BooleanExpression searchPost(String query) {
        if (query == null) {
            return null;
        }
        return room.name.containsIgnoreCase(query)
                .or(room.description.containsIgnoreCase(query));
    }

    private BooleanExpression checkFilter(PreviewFilter filter) {
        if (filter == null) {
            return null;
        }
        return switch (filter) {
            case RECRUITING -> room.recruitPostTypeEnum.eq(RecruitPostTypeEnum.RECRUITING);
            case RECRUITMENT_COMPLETED -> room.recruitPostTypeEnum.eq(RecruitPostTypeEnum.RECRUITMENT_COMPLETED);
            case PEOPLE_LIMITED -> room.peopleLimited.eq(true);
            case PEOPLE_NOT_LIMITED -> room.peopleLimited.eq(false);
            default -> throw new RestApiException(GlobalErrorStatus._FILTER_NOT_VALID);
        };
    }

    private BooleanExpression lastStudyValue(SortType sortType, Long value) {
        if (value == null || value == 0) {
            return null;
        }
        return switch (sortType) {
            case RECENT -> room.id.lt(value);
            case LIKE -> room.likes.lt(value);
            case HIT -> room.views.lt(value);
            default -> throw new RestApiException(GlobalErrorStatus._SORT_TYPE_NOT_VALID);
        };
    }

    private OrderSpecifier<?>[] getOrderSpecifier(SortType sortType) {
        if (sortType == null) {
            return null;
        }
        return switch (sortType) {
            case RECENT -> new OrderSpecifier<?>[]{room.id.desc()};
            case LIKE -> new OrderSpecifier<?>[]{room.likes.desc(), room.id.desc()};
            case HIT -> new OrderSpecifier<?>[]{room.views.desc(), room.id.desc()};
            default -> throw new RestApiException(GlobalErrorStatus._SORT_TYPE_NOT_VALID);
        };
    }

    private Long getLastValue(List<Room> results, SortType sortType) {
        if (results.isEmpty()) {
            return null;
        }
        return switch (sortType) {
            case RECENT -> results.get(results.size() - 1).getId();
            case LIKE -> (long) results.get(results.size() - 1).getLikes();
            case HIT -> (long) results.get(results.size() - 1).getViews();
            default -> throw new RestApiException(GlobalErrorStatus._SORT_TYPE_NOT_VALID);
        };
    }
}

