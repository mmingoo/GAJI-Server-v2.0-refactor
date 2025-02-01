package gaji.service.oauth2.dto;

import gaji.service.domain.enums.ServiceRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final String provider;
    private final ServiceRole role;
    private final Boolean isNewUser;

    public CustomOAuth2User(String provider, ServiceRole role, Boolean isNewUser) {
        this.provider = provider;
        this.role = role;
        this.isNewUser = isNewUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap(); // 유저 정보 없음
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 없음
    }

    @Override
    public String getName() {
        return provider; // 유저 정보가 없으므로 provider 값 반환
    }

    public boolean getIsNewUser() {
        return isNewUser;
    }
}