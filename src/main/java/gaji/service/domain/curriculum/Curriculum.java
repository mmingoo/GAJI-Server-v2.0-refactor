package gaji.service.domain.curriculum;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    protected String body;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<CurriculumFile> curriculumFileList = new ArrayList<>();


}
