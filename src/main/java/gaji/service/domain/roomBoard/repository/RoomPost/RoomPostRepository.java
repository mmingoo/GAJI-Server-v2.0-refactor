package gaji.service.domain.roomBoard.repository.RoomPost;

import gaji.service.domain.roomBoard.entity.RoomPost.RoomPost;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto.PostSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomPostRepository extends JpaRepository<RoomPost, Long> {
    @Query("SELECT new gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto$PostSummaryDto(" +
            "r.id, r.title, r.studyMate.user.nickname, r.createdAt, r.viewCount, SIZE(r.postCommentList)) " +
            "FROM RoomPost r " +
            "WHERE r.roomBoard.id = :boardId AND r.createdAt <= :lastCreatedAt " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<PostSummaryDto> findPostSummariesForInfiniteScroll(
            @Param("boardId") Long boardId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            Pageable pageable);

    @Query("SELECT CASE " +
            "WHEN EXISTS (SELECT 1 FROM RoomPost r WHERE r.roomBoard.id = :boardId AND r.id = :postId) " +
            "THEN (SELECT r.createdAt FROM RoomPost r WHERE r.roomBoard.id = :boardId AND r.id = :postId) " +
            "ELSE (SELECT MAX(r.createdAt) FROM RoomPost r WHERE r.roomBoard.id = :boardId) " +
            "END")
    Optional<LocalDateTime> findCreatedAtByIdOrEarliest(@Param("boardId") Long boardId, @Param("postId") Long postId);



    @Query("SELECT new gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto$MainPostSummaryDto(" +
            "rp.id, rp.title, rp.body, rp.studyMate.user.nickname, rp.createdAt, rp.viewCount) " +
            "FROM RoomPost rp " +
            "WHERE rp.roomBoard.id = :boardId " +
            "ORDER BY rp.createdAt DESC")
    List<RoomPostResponseDto.MainPostSummaryDto> findLatestPostsSummary(@Param("boardId") Long boardId, Pageable pageable);
}
