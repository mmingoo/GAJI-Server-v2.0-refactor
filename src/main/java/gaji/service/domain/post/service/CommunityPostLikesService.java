package gaji.service.domain.post.service;

import gaji.service.domain.post.entity.CommnuityPost;

public interface CommunityPostLikesService {
    boolean existsByUserAndPost(Long userId, CommnuityPost post);
}
