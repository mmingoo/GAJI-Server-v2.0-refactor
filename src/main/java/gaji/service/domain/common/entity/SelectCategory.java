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
public class SelectCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private PostTypeEnum type;

    @Builder
    public SelectCategory(Category category, Long entityId, PostTypeEnum type) {
        this.category = category;
        this.entityId = entityId;
        this.type = type;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}