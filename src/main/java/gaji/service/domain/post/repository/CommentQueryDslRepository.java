package gaji.service.domain.post.repository;

import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.CommunityComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface CommentQueryDslRepository {
    Slice<CommunityComment> findBySliceAndPostFetchJoinWithUser(Integer lastGroupNum, CommnuityPost post, Pageable pageable);
}
