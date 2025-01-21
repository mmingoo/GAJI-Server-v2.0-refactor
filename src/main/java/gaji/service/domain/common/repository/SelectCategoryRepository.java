package gaji.service.domain.common.repository;

import gaji.service.domain.common.entity.SelectCategory;
import gaji.service.domain.enums.PostTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectCategoryRepository extends JpaRepository<SelectCategory, Long>, SelectCategoryQueryDslRepository {

    SelectCategory findByEntityIdAndType(Long entityId, PostTypeEnum type);

    void deleteByEntityIdAndType(Long entityId, PostTypeEnum type);

    boolean existsByEntityIdAndType(Long entityId, PostTypeEnum type);
}
