package gaji.service.domain.recruit.repository;

import gaji.service.domain.recruit.entity.RecruitPostBookmark;
import gaji.service.domain.room.entity.Room;
import gaji.service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitPostBookmarkRepository extends JpaRepository<RecruitPostBookmark, Long> {
    boolean existsByUserAndRoom(User user, Room room);

    void deleteByUserAndRoom(User user, Room room);
}
