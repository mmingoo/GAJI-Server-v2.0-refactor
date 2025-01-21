package gaji.service.temp.service;

import gaji.service.temp.dto.VoteRequest;
import gaji.service.temp.entity.GajiVote;
import gaji.service.temp.repository.GajiVoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final GajiVoteRepository gajiVoteRepository;

    // 캐릭터 이름을 받아 새로운 투표를 추가하거나 기존 투표의 카운트를 증가시키는 메서드
    @Transactional
    public String put (VoteRequest.VotePutDTO request) {
        gajiVoteRepository.save(
                GajiVote.builder()
                        .characterName(request.getCharacterName())
                        .email(request.getEmail())
                        .voteCount(0)
                        .build()
        );

        return "성공!";
    }

    // 캐릭터 이름 리스트 조회 메서드 (득표순)
    public List<GajiVote> get() {
        return gajiVoteRepository.findAllByOrderByVoteCountDesc();
    }

    // 특정 캐릭터 ID로 투표를 증가시키는 메서드
    @Transactional
    public String vote(Long characterId) {
        GajiVote gajiVote = gajiVoteRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid character ID"));

        gajiVote.plusVoteCount();
        gajiVoteRepository.save(gajiVote);

        return "성공!";
    }
}
