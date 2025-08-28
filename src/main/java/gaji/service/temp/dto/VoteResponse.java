package gaji.service.temp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class VoteResponse {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CharacterNameListDTO{

        private List<CharacterNameDTO> characterNameList;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CharacterNameDTO{

        @NotNull
        private String characterName;

        private int voteCount;

        @NotNull
        private Long characterId;
    }


}
