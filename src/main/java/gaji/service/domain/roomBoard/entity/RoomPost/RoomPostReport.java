package gaji.service.domain.roomBoard.entity.RoomPost;

import gaji.service.domain.enums.ReportPostTypeEnum;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomPostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private RoomPost roomPost;

    @Enumerated(EnumType.STRING)
    private ReportPostTypeEnum reportPostTypeEnum;

    private String description;
}
