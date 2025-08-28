package gaji.service.domain.recruit.repository;

import gaji.service.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Room, Long>, RecruitCustomRepository {
}
