package gaji.service.temp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class VoteRequest {
    @Getter
    public static class VotePutDTO{

        @NotNull
        private String characterName;

        private String email;
    }
}
