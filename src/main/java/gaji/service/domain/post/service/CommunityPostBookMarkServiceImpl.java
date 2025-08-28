package gaji.service.domain.post.service;

import gaji.service.domain.post.entity.CommunityPost;
import gaji.service.domain.post.repository.CommunityPostBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostBookMarkServiceImpl implements CommunityPostBookMarkService {
    private final CommunityPostBookmarkRepository postBookmarkRepository;

    @Override
    public boolean existsByUserAndPost(Long userId, CommunityPost post) {
        return postBookmarkRepository.existsByUserIdAndPost(userId, post);
    }
}
