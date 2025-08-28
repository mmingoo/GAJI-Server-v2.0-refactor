package gaji.service.domain.term;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // True : 동의 / False : 미동의
    private boolean isRequired;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL)
    private List<TermAgree> termAgreeList = new ArrayList<>();

    private String content;
}
