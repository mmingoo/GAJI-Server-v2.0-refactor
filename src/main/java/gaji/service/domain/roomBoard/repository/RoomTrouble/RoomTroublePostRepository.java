package gaji.service.domain.roomBoard.repository.RoomTrouble;

import gaji.service.domain.roomBoard.entity.RoomTrouble.RoomTroublePost;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTroublePostRepository extends JpaRepository<RoomTroublePost, Long> {
    @Query("SELECT new gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto$TroublePostSummaryDto(" +
            "r.id, r.title, r.studyMate.user.nickname, r.createdAt, r.viewCount, SIZE(r.troublePostCommentList)) " +
            "FROM RoomTroublePost r " +
            "WHERE r.roomBoard.id = :boardId AND r.createdAt <= :lastCreatedAt " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<RoomPostResponseDto.TroublePostSummaryDto> findTroublePostSummariesForInfiniteScroll(
            @Param("boardId") Long boardId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            Pageable pageable);

    @Query("SELECT CASE " +
            "WHEN EXISTS (SELECT 1 FROM RoomTroublePost r WHERE r.roomBoard.id = :boardId AND r.id = :postId) " +
            "THEN (SELECT r.createdAt FROM RoomTroublePost r WHERE r.roomBoard.id = :boardId AND r.id = :postId) " +
            "ELSE (SELECT MAX(r.createdAt) FROM RoomTroublePost r WHERE r.roomBoard.id = :boardId) " +
            "END")
    Optional<LocalDateTime> findCreatedAtByIdOrEarliest(@Param("boardId") Long boardId, @Param("postId") Long postId);}
