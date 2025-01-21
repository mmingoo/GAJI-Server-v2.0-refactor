package gaji.service.domain.roomBoard.repository.RoomTrouble;

import gaji.service.domain.roomBoard.entity.RoomTrouble.TroublePostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TroublePostCommentRepository extends JpaRepository<TroublePostComment, Long> {
    @Query("SELECT c FROM TroublePostComment c " +
            "LEFT JOIN FETCH c.replies " +
            "WHERE c.roomTroublePost.id = :postId " +
            "AND c.isReply = false " +
            "AND (:lastCommentId IS NULL OR c.id > :lastCommentId) " +
            "ORDER BY c.createdAt ASC, c.id ASC")
    List<TroublePostComment> findCommentsWithReplies(@Param("postId") Long postId,
                                                     @Param("lastCommentId") Long lastCommentId,
                                                     Pageable pageable);

    @Query("SELECT c FROM TroublePostComment c " +
            "WHERE c.roomTroublePost.id = :postId " +
            "AND c.isReply = false " +
            "ORDER BY c.createdAt ASC, c.id ASC")
    Page<TroublePostComment> findOldestComments(@Param("postId") Long postId, Pageable pageable);

}
