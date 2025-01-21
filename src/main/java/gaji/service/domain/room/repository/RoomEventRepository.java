package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.Room;
import gaji.service.domain.room.entity.RoomEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomEventRepository extends JpaRepository<RoomEvent,Long> {

    Optional<RoomEvent> findRoomEventByRoomIdAndWeeks(Long roomId, Integer weeks);
    Optional<RoomEvent> findRoomEventById(Long roomId);

    List<RoomEvent> findByRoom(Room room);
}
