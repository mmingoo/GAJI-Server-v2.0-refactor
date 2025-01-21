package gaji.service.domain.post.service;

import gaji.service.domain.post.entity.CommnuityPost;
import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.entity.PostBookmark;
import gaji.service.domain.post.entity.PostLikes;
import gaji.service.domain.post.web.dto.CommunityPostCommentResponseDTO;
import gaji.service.domain.post.web.dto.CommunityPostRequestDTO;
import gaji.service.domain.post.web.dto.CommunityPostResponseDTO;

public interface CommunityPostCommandService {

    CommunityPostResponseDTO.PostIdResponseDTO uploadPost(Long userId, CommunityPostRequestDTO.UploadPostRequestDTO request);
    CommunityPostResponseDTO.PostIdResponseDTO editPost(Long userId, Long postId, CommunityPostRequestDTO.EditPostRequestDTO request);
    CommunityPostCommentResponseDTO.WriteCommentResponseDTO writeCommentOnCommunityPost(Long userId, Long postId, Long parentCommentId, CommunityPostRequestDTO.WriteCommentRequestDTO request);
    void hardDeleteComment(Long userId, Long commentId);
    void hardDeleteCommunityPost(Long userId, Long postId);
    CommunityPostResponseDTO.PostBookmarkIdDTO bookmarkCommunityPost(Long userId, Long postId);
    void cancelbookmarkCommunityPost(Long userId, Long postId);
    CommunityPostResponseDTO.PostLikesIdDTO likeCommunityPost(Long userId, Long postId);
    void cancelLikeCommunityPost(Long userId, Long postId);

}
