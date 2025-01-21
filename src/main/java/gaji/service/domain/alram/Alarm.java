package gaji.service.domain.alram;

import gaji.service.domain.enums.IsConfirmed;
import gaji.service.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "alarm", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RoomAlarm roomAlarm;

    @OneToOne(mappedBy = "alarm", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserAlarm userAlarm;

    @Enumerated(EnumType.STRING)
    private IsConfirmed isConfirmed;


}
