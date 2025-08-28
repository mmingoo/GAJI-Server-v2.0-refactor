package gaji.service.domain.post.repository;

import gaji.service.domain.post.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentJpaRepository extends JpaRepository<CommunityComment, Long>, CommentQueryDslRepository {
}
