package gaji.service.oauth2.dto;

import gaji.service.domain.enums.Gender;
import gaji.service.domain.enums.ServiceRole;
import gaji.service.domain.enums.SocialType;
import gaji.service.domain.enums.UserActive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransferUserDTO {

    private ServiceRole role;
    // 서버에서 발급받는 아이디
    private String usernameId;
    private SocialType socialType;
    private UserActive userActive;
}
