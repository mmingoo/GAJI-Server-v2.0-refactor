package gaji.service.domain.post.converter;

import gaji.service.domain.post.entity.CommunityComment;
import gaji.service.domain.post.service.CommunityCommentService;
import gaji.service.domain.post.web.dto.CommunityPostCommentResponseDTO;
import gaji.service.global.converter.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CommunityCommentConverter {
    private final CommunityCommentService communityCommentService;

    public static CommunityPostCommentResponseDTO.WriteCommentResponseDTO toWriteCommentResponseDTO(CommunityComment comment) {
        return CommunityPostCommentResponseDTO.WriteCommentResponseDTO.builder()
                .commentId(comment.getId())
                .build();
    }

    public static CommunityPostCommentResponseDTO.PostCommentDTO toPostCommentDTO(CommunityComment comment, boolean isWriter) {
        return CommunityPostCommentResponseDTO.PostCommentDTO.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .userNickName(comment.getUser().getNickname())
                .commentBody(comment.getBody())
                .groupNum(comment.getGroupNum())
                .depth(comment.getDepth())
                .isWriter(isWriter)
                .commentWriteDate(DateConverter.convertToRelativeTimeFormat(comment.getCreatedAt())+" 작성")
                .profileImageUrl(comment.getUser().getProfileImagePth())
                .build();
    }

    public CommunityPostCommentResponseDTO.PostCommentListDTO toPostCommentListDTO(List<CommunityComment> commentList, boolean hasNext, Long userId) {
        List<CommunityPostCommentResponseDTO.PostCommentDTO> postCommentDTOList = new ArrayList<>();

        for (CommunityComment communityComment : commentList) {
            boolean isWriter = userId != null && communityCommentService.isCommentWriter(userId, communityComment);
            postCommentDTOList.add(CommunityCommentConverter.toPostCommentDTO(communityComment, isWriter));
        }

        return CommunityPostCommentResponseDTO.PostCommentListDTO.builder()
                .commentList(postCommentDTOList)
                .hasNext(hasNext)
                .build();
    }
}
