package gaji.service.domain.roomBoard.repository.RoomInfo;

import gaji.service.domain.roomBoard.entity.RoomInfo.InfoPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoPostCommentRepository extends JpaRepository<InfoPostComment, Long> {

    @Query("SELECT c FROM InfoPostComment c " +
            "LEFT JOIN FETCH c.replies " +
            "WHERE c.roomInfoPost.id = :postId " +
            "AND c.parentComment IS NULL " +
            "ORDER BY c.createdAt ASC, c.id ASC")
    Page<InfoPostComment> findOldestComments(@Param("postId") Long postId, Pageable pageable);
}
