package gaji.service.oauth2.dto;

import gaji.service.domain.enums.ServiceRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserDTO {

    private ServiceRole role;
    private String name;
    // 서버에서 발급받는 아이디
    private String usernameId;
    private boolean isNewUser;
}