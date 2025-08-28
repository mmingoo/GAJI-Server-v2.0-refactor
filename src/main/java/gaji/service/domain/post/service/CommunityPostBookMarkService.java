package gaji.service.domain.post.service;

import gaji.service.domain.post.entity.CommunityPost;

public interface CommunityPostBookMarkService {

    boolean existsByUserAndPost(Long userId, CommunityPost post);
}
