package gaji.service.domain.room.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gaji.service.domain.room.entity.QRoom;
import gaji.service.domain.studyMate.entity.QStudyMate;
import gaji.service.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
public class RoomCustomRepositoryImpl implements RoomCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QRoom room = QRoom.room;
    private final QStudyMate studyMate = QStudyMate.studyMate;

    public Slice<Tuple> findAllOngoingRoomsByUser(User user, LocalDate cursorDate, Long cursorId, Pageable pageable) {
        List<Long> userRoomIds = jpaQueryFactory
                .select(studyMate.room.id)
                .from(studyMate)
                .where(studyMate.user.eq(user))
                .fetch();

        List<Tuple> ongoingRooms = jpaQueryFactory.select(room.id, room.name, room.description, room.thumbnailUrl, room.studyStartDay)
                .from(room)
                .where(room.id.in(userRoomIds)
                        .and(room.studyEndDay.after(getCurrentDay().minusDays(1)))
                        .and(getCursorCondition(cursorDate, cursorId)))
                .orderBy(room.studyStartDay.desc(), room.id.asc())
                .limit(pageable.getPageSize()+1) // size보다 1개 더 가져와서 다음 페이지 여부 확인
                .fetch();

        return checkLastPage(pageable, ongoingRooms);
    }

    public Slice<Tuple> findAllOngoingRoomsByUser(User user, Pageable pageable) {
        List<Long> userRoomIds = jpaQueryFactory
                .select(studyMate.room.id)
                .from(studyMate)
                .where(studyMate.user.eq(user))
                .fetch();

        List<Tuple> ongoingRooms = jpaQueryFactory.select(room.id, room.name, room.description, room.thumbnailUrl, room.studyStartDay)
                .from(room)
                .where(room.id.in(userRoomIds)
                        .and(room.studyEndDay.after(getCurrentDay().minusDays(1))))
                .orderBy(room.studyStartDay.desc(), room.id.asc())
                .limit(pageable.getPageSize()+1) // size보다 1개 더 가져와서 다음 페이지 여부 확인
                .fetch();

        return checkLastPage(pageable, ongoingRooms);
    }


    public Slice<Tuple> findAllEndedRoomsByUser(User user, LocalDate cursorDate, Long cursorId, Pageable pageable) {
        List<Long> userRoomIds = jpaQueryFactory
                .select(studyMate.room.id)
                .from(studyMate)
                .where(studyMate.user.eq(user))
                .fetch();

        List<Tuple> endedRooms = jpaQueryFactory.select(room.id, room.name, room.description, room.thumbnailUrl, room.studyStartDay)
                .from(room)
                .where(room.id.in(userRoomIds)
                        .and(room.studyEndDay.before(getCurrentDay()))
                        .and(getCursorCondition(cursorDate, cursorId)))
                .orderBy(room.studyStartDay.desc(), room.id.asc())
                .limit(pageable.getPageSize()+1) // size보다 1개 더 가져와서 다음 페이지 여부 확인
                .fetch();

        return checkLastPage(pageable, endedRooms);
    }

    public Slice<Tuple> findAllEndedRoomsByUser(User user, Pageable pageable) {
        List<Long> userRoomIds = jpaQueryFactory
                .select(studyMate.room.id)
                .from(studyMate)
                .where(studyMate.user.eq(user))
                .fetch();

        List<Tuple> endedRooms = jpaQueryFactory.select(room.id, room.name, room.description, room.thumbnailUrl, room.studyStartDay)
                .from(room)
                .where(room.id.in(userRoomIds)
                        .and(room.studyEndDay.before(getCurrentDay())))
                .orderBy(room.studyStartDay.desc(), room.id.asc())
                .limit(pageable.getPageSize()+1) // size보다 1개 더 가져와서 다음 페이지 여부 확인
                .fetch();

        return checkLastPage(pageable, endedRooms);
    }


    private Slice<Tuple> checkLastPage(Pageable pageable, List<Tuple> roomList) {
        boolean hasNext = false;

        if (roomList.size() > pageable.getPageSize()) {
            hasNext = true;
            roomList.remove(pageable.getPageSize()); // 더 가져왔을 시, 삭제
        }
        return new SliceImpl<Tuple>(roomList, pageable, hasNext);
    }

    private BooleanExpression getCursorCondition(LocalDate cursorDate, Long cursorId) {
        return (room.studyStartDay.eq(cursorDate).and(room.id.gt(cursorId)))
                .or(room.studyStartDay.lt(cursorDate));
    }

    private LocalDate getCurrentDay() {
        return LocalDate.now();
    }

}
