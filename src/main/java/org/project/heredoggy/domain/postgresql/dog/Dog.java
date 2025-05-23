package org.project.heredoggy.domain.postgresql.dog;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String personality; // 특이사항

    private Double weight;

    private Boolean isNeutered; // 중성화 여부

    private String foundLocation; // 발견 장소

    @Enumerated(EnumType.STRING)
    private DogStatus status = DogStatus.AVAILABLE; // 현재 상태(사용 가능)

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 등록 일시

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 일자

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dog_id")
    private List<DogImage> images = new ArrayList<>();


}
