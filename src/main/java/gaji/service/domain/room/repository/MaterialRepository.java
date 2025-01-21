package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.Material;
import gaji.service.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    void deleteAllByRoom(Room room);
}
