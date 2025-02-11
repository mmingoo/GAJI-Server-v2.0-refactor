package gaji.service.jwt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gaji.service.jwt.entity.RefreshEntity;
import gaji.service.jwt.filter.JWTUtil;
import gaji.service.jwt.rpository.RefreshRepository;
import gaji.service.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper;

    @Value("${redirectionUrl}")
    private String redirectionUrl;

    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String usernameId = customUserDetails.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        String accessToken = jwtUtil.createJwt("access", usernameId, role, 600000000L);
        String refreshToken = jwtUtil.createJwt("refresh", usernameId, role, 86400000000L);

        if (!refreshRepository.existsByUsername(usernameId)) {
            addRefreshEntity(usernameId, refreshToken, 86400000000L);
        }

        // Refresh 토큰을 HttpOnly 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 사용
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(8640000); // 24시간
        response.addCookie(refreshTokenCookie);


        // 1. 헤더로 보낼 경우
        response.setHeader("Authorization", "Bearer " + accessToken);



        // 2. body에 담아서 보낼 경우 Access 토큰을 JSON 응답으로 전송
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
        response.setStatus(HttpStatus.OK.value());

        // 토큰 로그로 남기기
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);


        String finalRedirectionUrl = this.redirectionUrl;

        // 리다이렉션 URL 생성
        String targetUrl = UriComponentsBuilder.fromUriString(finalRedirectionUrl)
                .queryParam("access_token", accessToken)
                .build().toUriString();

        // 리다이렉션 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }
}