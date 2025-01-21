package gaji.service.domain.user.service;


import com.querydsl.core.Tuple;
import gaji.service.domain.enums.PostTypeEnum;
import gaji.service.domain.enums.RoomTypeEnum;
import gaji.service.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserQueryService {

    boolean existUserById(Long userId);
    User findUserById(Long userId);
    Slice<Tuple> getUserRoomList(Long userId, LocalDate cursorDate, Long cursorId, RoomTypeEnum type, int size);
    User getUserDetail(Long userId);
    Slice<Tuple> getUserPostList(Long userId, LocalDateTime cursorDateTime, PostTypeEnum type, int size);

    User findByUsernameId(String usernameId);
}
