package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.RoomNotice;
import gaji.service.domain.room.web.dto.response.NoticeDto;
import gaji.service.domain.room.web.dto.response.RoomResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomNoticeRepository extends JpaRepository<RoomNotice, Long> {
    @Query("SELECT NEW gaji.service.domain.room.web.dto.response.NoticeDto(" +
            "rn.id, sm.user.nickname, rn.title, rn.body, CAST(COUNT(nc) AS Long), rn.createdAt, rn.viewCount) " +
            "FROM RoomNotice rn " +
            "JOIN rn.studyMate sm " +
            "LEFT JOIN NoticeConfirmation nc ON nc.roomNotice.id = rn.id " +
            "WHERE sm.room.id = :roomId AND rn.createdAt <= :lastCreatedAt " +
            "GROUP BY rn.id, sm.user.nickname, rn.title, rn.body, rn.createdAt, rn.viewCount " +
            "ORDER BY rn.createdAt DESC, rn.id DESC")
    List<NoticeDto> findNoticeSummariesForInfiniteScroll(
            @Param("roomId") Long roomId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            Pageable pageable);

    @Query("SELECT CASE " +
            "WHEN EXISTS (SELECT 1 FROM RoomNotice rn WHERE rn.studyMate.room.id = :roomId AND rn.id = :noticeId) " +
            "THEN (SELECT rn.createdAt FROM RoomNotice rn WHERE rn.studyMate.room.id = :roomId AND rn.id = :noticeId) " +
            "ELSE (SELECT MAX(rn.createdAt) FROM RoomNotice rn WHERE rn.studyMate.room.id = :roomId) " +
            "END")
    Optional<LocalDateTime> findCreatedAtByIdOrEarliest(@Param("roomId") Long roomId, @Param("noticeId") Long noticeId);
}
