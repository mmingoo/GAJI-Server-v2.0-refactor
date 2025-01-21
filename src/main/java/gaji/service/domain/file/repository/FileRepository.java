package gaji.service.domain.file.repository;

import gaji.service.domain.file.domain.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files, Long> {
    Files findByPath(String fileUrl);
}
