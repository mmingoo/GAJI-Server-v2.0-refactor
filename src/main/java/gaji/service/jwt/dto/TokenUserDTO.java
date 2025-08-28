package gaji.service.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenUserDTO {
    private String usernameId;
    private String role;
}
