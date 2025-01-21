package gaji.service.domain.common.entity;

import gaji.service.domain.enums.PostTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectHashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private PostTypeEnum type;

    @Builder
    public SelectHashtag(Hashtag hashtag, Long entityId, PostTypeEnum type) {
        this.hashtag = hashtag;
        this.entityId = entityId;
        this.type = type;
    }
}
