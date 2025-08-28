package gaji.service.domain;

import gaji.service.domain.enums.IsRevoked;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private IsRevoked isRevoked;
}
