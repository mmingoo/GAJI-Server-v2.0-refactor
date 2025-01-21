package gaji.service.domain.common.repository;

import gaji.service.domain.common.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByName(String name);
    boolean existsByName(String name);
}
