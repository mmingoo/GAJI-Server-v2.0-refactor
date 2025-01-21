package gaji.service.domain.recruit.repository;

import gaji.service.domain.enums.CategoryEnum;
import gaji.service.domain.enums.PreviewFilter;
import gaji.service.domain.enums.SortType;
import gaji.service.domain.recruit.web.dto.RecruitResponseDTO;
import org.springframework.data.domain.Pageable;

public interface RecruitCustomRepository {

    RecruitResponseDTO.PreviewListResponseDTO findByCategoryOrderBySortType(
            CategoryEnum category, PreviewFilter filter, SortType sortType, String query, Long value, Pageable pageable);

    RecruitResponseDTO.DefaultPreviewDTO findByCategory(
            CategoryEnum category, Pageable pageable);
}
