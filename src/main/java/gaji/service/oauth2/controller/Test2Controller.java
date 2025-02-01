package gaji.service.oauth2.controller;

import gaji.service.domain.enums.ServiceRole;
import gaji.service.jwt.filter.JWTUtil;
import gaji.service.jwt.service.CustomSuccessHandler;
import gaji.service.jwt.service.TokenProviderService;
import gaji.service.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
public class Test2Controller {

    private final CustomSuccessHandler customSuccessHandler;
    private final TokenProviderService tokenProviderService;

    public Test2Controller(CustomSuccessHandler customSuccessHandler, TokenProviderService tokenProviderService, JWTUtil jwtUtil, TokenProviderService tokenProviderService1) {
        this.customSuccessHandler = customSuccessHandler;
        this.tokenProviderService = tokenProviderService1;
    }

    @GetMapping("/my")
    public ResponseEntity<Long> getUser(@RequestHeader("Authorization") String authorizationHeader) {
            Long userId = tokenProviderService.getUserIdFromToken(authorizationHeader);
            return ResponseEntity.ok(userId);

    }
//    @GetMapping("/auth-success")
//    public void testAuthSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User("testUser", ServiceRole.ROLE_USER);
//        Authentication authentication = new TestAuthentication(customOAuth2User);
//
//        customSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//    }

    private static class TestAuthentication implements Authentication {
        private final CustomOAuth2User customOAuth2User;

        public TestAuthentication(CustomOAuth2User customOAuth2User) {
            this.customOAuth2User = customOAuth2User;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return customOAuth2User.getAuthorities();
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return customOAuth2User;
        }

        @Override
        public boolean isAuthenticated() {
            return true;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        }

        @Override
        public String getName() {
            return customOAuth2User.getName();
        }
    }
}