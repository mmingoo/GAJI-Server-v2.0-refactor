package gaji.service.domain.roomBoard.repository.RoomPost;

import gaji.service.domain.roomBoard.entity.RoomPost.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("SELECT c FROM PostComment c " +
            "LEFT JOIN FETCH c.replies " +
            "WHERE c.roomPost.id = :postId " +
            "AND c.parentComment IS NULL " +
            "ORDER BY c.createdAt ASC, c.id ASC")
    Page<PostComment> findOldestComments(@Param("postId") Long postId, Pageable pageable);
}
