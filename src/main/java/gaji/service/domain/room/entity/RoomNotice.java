package gaji.service.domain.room.entity;

import gaji.service.domain.studyMate.entity.StudyMate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "study_mate_id")
    private StudyMate studyMate;

    @OneToMany(mappedBy = "roomNotice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeConfirmation> confirmations = new ArrayList<>();


    private String title;

    private String body;

    private Integer viewCount;
    private Integer confirmCount;

    private LocalDateTime createdAt;


    //생성
    @PrePersist
    public void prePersist() {
        this.viewCount = 0;
        this.confirmCount = 0;
        this.createdAt = LocalDateTime.now();
    }
    public void incrementConfirmCount() {
        this.confirmCount++;
    }

    public void decrementConfirmCount() {
        if (this.confirmCount > 0) {
            this.confirmCount--;
        }
    }
    public void updateBody(String body) {
        this.body = body;
    }
}
