package org.project.heredoggy.domain.postgresql.dog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    @Lob // Large Object
    @Column(columnDefinition = "BYTEA") // 명시적으로 Bytea
    private byte[] featureVector; // 이미지 특정 벡터를 저장할 용도

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;
}
