package gaji.service.domain.post.repository;

import gaji.service.domain.post.entity.CommunityPost;
import gaji.service.domain.post.entity.CommunityComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface CommentQueryDslRepository {
    Slice<CommunityComment> findBySliceAndPostFetchJoinWithUser(Integer lastGroupNum, CommunityPost post, Pageable pageable);
}
