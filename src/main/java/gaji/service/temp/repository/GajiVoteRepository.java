package gaji.service.temp.repository;

import gaji.service.temp.entity.GajiVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GajiVoteRepository extends JpaRepository<GajiVote, Long> {

    // 투표 수 기준으로 캐릭터를 내림차순 정렬하여 가져오는 메서드 (예시)
    List<GajiVote> findAllByOrderByVoteCountDesc();
}
