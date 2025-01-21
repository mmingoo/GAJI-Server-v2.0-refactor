package gaji.service.domain.post.repository;


import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.PostLikes;
import gaji.service.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostLikesRepository extends JpaRepository<PostLikes, Long> {
    void deleteByUserAndPost(User user, CommnuityPost post);
    boolean existsByUserIdAndPost(Long userId, CommnuityPost post);
}
