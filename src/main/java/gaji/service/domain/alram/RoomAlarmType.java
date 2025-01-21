package gaji.service.domain.alram;

import gaji.service.domain.enums.RoomAlarmTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomAlarmType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomAlarmTypeEnum type;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "type")
    private RoomAlarm roomAlarm;

}
