package gaji.service.domain.room.repository;

import gaji.service.domain.room.entity.NoticeConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeConfirmationRepository extends JpaRepository<NoticeConfirmation, Long> {
    NoticeConfirmation findByRoomNoticeIdAndStudyMateId(Long roomNoticeId, Long studyMateId);

    @Query("SELECT DISTINCT u.nickname FROM NoticeConfirmation nc " +
            "JOIN nc.studyMate sm " +
            "JOIN sm.user u " +
            "WHERE nc.roomNotice.id = :noticeId")
    List<String> findConfirmedUserNicknamesByNoticeId(@Param("noticeId") Long noticeId);
}