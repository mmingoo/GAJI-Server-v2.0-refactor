package gaji.service.domain.alram;

import gaji.service.domain.enums.UserAlarmTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAlarmType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserAlarmTypeEnum type;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userAlarmType")
    private UserAlarm userAlarm;
}
