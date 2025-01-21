package gaji.service.domain.post.repository;

import gaji.service.domain.post.entity.CommnuityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostJpaRepository extends JpaRepository<CommnuityPost, Long>, CommunityPostQueryDslRepository {

}
