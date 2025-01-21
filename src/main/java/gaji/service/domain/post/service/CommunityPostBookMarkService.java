package gaji.service.domain.post.service;

import gaji.service.domain.post.entity.CommnuityPost;

public interface CommunityPostBookMarkService {

    boolean existsByUserAndPost(Long userId, CommnuityPost post);
}
