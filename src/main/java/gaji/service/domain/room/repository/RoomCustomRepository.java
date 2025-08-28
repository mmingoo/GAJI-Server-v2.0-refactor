package gaji.service.domain.room.repository;

import com.querydsl.core.Tuple;
import gaji.service.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface RoomCustomRepository {
    public Slice<Tuple> findAllOngoingRoomsByUser(User user, LocalDate cursorDate, Long cursorId, Pageable pageable);
    public Slice<Tuple> findAllOngoingRoomsByUser(User user, Pageable pageable);
    public Slice<Tuple> findAllEndedRoomsByUser(User user, LocalDate cursorDate, Long cursorId, Pageable pageable);
    public Slice<Tuple> findAllEndedRoomsByUser(User user, Pageable pageable);
}
