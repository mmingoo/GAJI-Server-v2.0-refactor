package gaji.service.domain.post.repository;

import gaji.service.domain.post.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostJpaRepository extends JpaRepository<CommunityPost, Long>, CommunityPostQueryDslRepository {

}
