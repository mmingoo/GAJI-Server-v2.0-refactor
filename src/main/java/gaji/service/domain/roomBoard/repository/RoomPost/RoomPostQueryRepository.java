package gaji.service.domain.roomBoard.repository.RoomPost;

import gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomPostQueryRepository {
    @PersistenceContext
    private EntityManager em;

    public List<RoomPostResponseDto.PostListDto> findTop3RecentPostsWithUserInfo(Long roomId) {
        String jpql = """
            SELECT NEW gaji.service.domain.roomBoard.web.dto.RoomPostResponseDto$PostListDto(
                rp.id,
                rp.title,
                rp.body,
                rp.viewCount,
                rp.postTime,
                u.id,
                u.profileImagePth
            )
            FROM Room r
            JOIN r.roomBoardList rb
            JOIN rb.roomPostList rp
            JOIN rp.user u
            WHERE r.id = :roomId
            ORDER BY rp.postTime DESC
        """;
        return em.createQuery(jpql, RoomPostResponseDto.PostListDto.class)
                .setParameter("roomId", roomId)
                .setMaxResults(3)
                .getResultList();
    }
}
