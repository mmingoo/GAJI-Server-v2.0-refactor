package gaji.service.domain.myRepeat;

import gaji.service.domain.enums.Frequency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private LocalDate StartTime;
    private LocalDate EndTime;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;


}
