package gaji.service.domain.room.repository;

import gaji.service.domain.room.web.dto.RoomResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class RoomQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public List<RoomResponseDto.NoticeDto> getNotices(Long roomId, int page, int size) {
        String jpql = """
        SELECT NEW gaji.service.domain.room.web.dto.RoomResponseDto$NoticeDto(
            rn.id,
            sm.user.name,
            rn.title,
            rn.body,
        CAST(COUNT(nc) AS Long),
            rn.createdAt,
            rn.viewCount
        )
        FROM RoomNotice rn
        JOIN rn.studyMate sm
        LEFT JOIN NoticeConfirmation nc ON nc.roomNotice.id = rn.id
        WHERE sm.room.id = :roomId
        GROUP BY rn.id, sm.user.name, rn.title, rn.body, rn.createdAt, rn.viewCount
        ORDER BY rn.createdAt DESC
    """;
        List<RoomResponseDto.NoticeDto> notices = entityManager.createQuery(jpql, RoomResponseDto.NoticeDto.class)
                .setParameter("roomId", roomId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

        LocalDateTime now = LocalDateTime.now();
        for (RoomResponseDto.NoticeDto notice : notices) {
            notice.setTimeSincePosted(calculateTimeDifference(notice.getCreatedAt(), now));
        }

        return notices;
    }

    private String calculateTimeDifference(LocalDateTime createdAt, LocalDateTime now) {
        long minutes = ChronoUnit.MINUTES.between(createdAt, now);
        if (minutes < 60) return minutes + "분 전";
        long hours = ChronoUnit.HOURS.between(createdAt, now);
        if (hours < 24) return hours + "시간 전";
        long days = ChronoUnit.DAYS.between(createdAt, now);
        if (days < 7) return days + "일 전";
        long weeks = ChronoUnit.WEEKS.between(createdAt, now);
        if (weeks < 4) return weeks + "주 전";
        long months = ChronoUnit.MONTHS.between(createdAt, now);
        if (months < 12) return months + "개월 전";
        long years = ChronoUnit.YEARS.between(createdAt, now);
        return years + "년 전";
    }

    public void incrementViewCount(Long noticeId) {
        String jpql = "UPDATE RoomNotice rn SET rn.viewCount = rn.viewCount + 1 WHERE rn.id = :noticeId";
        entityManager.createQuery(jpql)
                .setParameter("noticeId", noticeId)
                .executeUpdate();
    }

    public void updateConfirmCount(Long noticeId) {
        String jpql = """
            UPDATE RoomNotice rn 
            SET rn.confirmCount = (
                SELECT COUNT(nc) 
                FROM NoticeConfirmation nc 
                WHERE nc.roomNotice.id = :noticeId
            ) 
            WHERE rn.id = :noticeId
        """;

        entityManager.createQuery(jpql)
                .setParameter("noticeId", noticeId)
                .executeUpdate();
    }


    public RoomResponseDto.RoomMainDto getMainStudyRoom(Long roomId) {
        String jpql = """
    SELECT NEW gaji.service.domain.room.web.dto.RoomResponseDto$RoomMainDto(
        r.name,
        r.studyStartDay,
        r.studyEndDay,
        r.recruitStartDay,
        r.recruitEndDay,
        CAST(FUNCTION('DATEDIFF', r.recruitEndDay, CURRENT_DATE) AS long),
        COUNT(DISTINCT sa.id)
    )
    FROM Room r
    LEFT JOIN r.studyApplicantList sa
    WHERE r.id = :roomId
    GROUP BY r.id, r.name, r.studyStartDay, r.studyEndDay, r.recruitStartDay, r.recruitEndDay
""";

        RoomResponseDto.RoomMainDto result = entityManager.createQuery(jpql, RoomResponseDto.RoomMainDto.class)
                .setParameter("roomId", roomId)
                .getSingleResult();

        return result;
    }



    public RoomResponseDto.MainRoomNoticeDto getRoomNotices(Long roomId) {
        // 최신 공지사항 조회
        String latestNoticeJpql = """
    SELECT NEW gaji.service.domain.room.web.dto.RoomResponseDto$MainRoomNoticeDto$NoticePreview(
        rn.id,
        rn.title,
        rn.title
    )
    FROM RoomNotice rn
    WHERE rn.studyMate.room.id = :roomId
    ORDER BY rn.id DESC
    """;
        List<RoomResponseDto.MainRoomNoticeDto.NoticePreview> latestNotices = entityManager.createQuery(latestNoticeJpql, RoomResponseDto.MainRoomNoticeDto.NoticePreview.class)
                .setParameter("roomId", roomId)
                .setMaxResults(1)
                .getResultList();

        Long latestNoticeId = null;
        String latestNoticeTitle = null;
        if (!latestNotices.isEmpty()) {
            RoomResponseDto.MainRoomNoticeDto.NoticePreview latestNotice = latestNotices.get(0);
            latestNoticeId = latestNotice.getId();
            latestNoticeTitle = latestNotice.getTitle();
        }

        // 최신 공지사항을 제외한 다음 4개 공지사항 제목과 내용 조회
        String noticePreviewJpql = """
        SELECT NEW gaji.service.domain.room.web.dto.RoomResponseDto$MainRoomNoticeDto$NoticePreview(
            rn.id,
            rn.title,
            CASE WHEN LENGTH(rn.body) > 30 THEN SUBSTRING(rn.body, 1, 30) || '...' ELSE rn.body END
        )
        FROM RoomNotice rn
        WHERE rn.studyMate.room.id = :roomId AND rn.id < :latestNoticeId
        ORDER BY rn.id DESC
        """;
        List<RoomResponseDto.MainRoomNoticeDto.NoticePreview> noticePreviews = entityManager.createQuery(noticePreviewJpql, RoomResponseDto.MainRoomNoticeDto.NoticePreview.class)
                .setParameter("roomId", roomId)
                .setParameter("latestNoticeId", latestNoticeId)
                .setMaxResults(4)
                .getResultList();

        return RoomResponseDto.MainRoomNoticeDto.builder()
                .latestNoticeId(latestNoticeId)
                .latestNoticeTitle(latestNoticeTitle)
                .noticePreviews(noticePreviews)
                .build();
    }


}