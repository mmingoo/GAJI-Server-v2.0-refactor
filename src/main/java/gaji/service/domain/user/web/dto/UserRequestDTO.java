package gaji.service.domain.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class UserRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class UpdateNicknameDTO {
        @NotBlank(message = "공백은 불가능합니다.") //추후 닉네임 제약으로 수정가능성 ex) 닉네임은 2~10글자
        private String nickname;
    }

}
