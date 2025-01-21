package gaji.service.temp.controller;

import gaji.service.domain.file.dto.response.FileDeleteResponse;
import gaji.service.global.base.BaseResponse;
import gaji.service.temp.dto.VoteRequest;
import gaji.service.temp.dto.VoteResponse;
import gaji.service.temp.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "캐릭터 이름 선정 API", description = "캐릭터 이름 선정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;

    @PostMapping(value = "")
    @Operation(summary = "캐릭터 이름 작성 API", description = "캐릭터 이름 작성함 성공하면 성공이라고 응답 옴")
    public BaseResponse<String> put ( @RequestBody VoteRequest.VotePutDTO request)
    {
        return BaseResponse.onSuccess(voteService.put(request));
    }

    @GetMapping
    @Operation(summary = "캐릭터 이름 리스트 조회 (득표순) API")
    public BaseResponse<VoteResponse.CharacterNameListDTO> get() {
        List<VoteResponse.CharacterNameDTO> characterNameDTOs = voteService.get().stream()
                .map(gajiVote -> VoteResponse.CharacterNameDTO.builder()
                        .characterName(gajiVote.getCharacterName())
                        .characterId(gajiVote.getId())
                        .voteCount(gajiVote.getVoteCount())
                        .build())
                .collect(Collectors.toList());


        return BaseResponse.onSuccess(new VoteResponse.CharacterNameListDTO(characterNameDTOs));
    }

    @GetMapping("{characterId}")
    @Operation(summary = "캐릭터 투표 API")
    public BaseResponse<String> vote(@PathVariable("characterId") Long characterId) {
        return BaseResponse.onSuccess(voteService.vote(characterId));
    }
}
