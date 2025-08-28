package gaji.service.jwt.service;

import gaji.service.domain.user.repository.UserRepository;
import gaji.service.jwt.filter.JWTUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenProviderService {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public TokenProviderService(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Long getUserIdFromToken(String authorizationHeader) {
        // "Bearer " 제거
        String token = authorizationHeader.replace("Bearer ", "");

        // 토큰에서 username 추출
        String username = jwtUtil.getUsername(token);

        // username으로 사용자 ID 조회
        return userRepository.findByUsernameId(username).getId();
    }
}
