package gaji.service.domain;

import gaji.service.domain.enums.ReportPostEnum;
import gaji.service.domain.enums.ReportPostTypeEnum;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReportPostTypeEnum reportType; // 신고 명목(사기, 광고 등등)

    @Enumerated(EnumType.STRING)
    private ReportPostEnum type; //신고할 글의 종류 (블로그, 게시글)


}
