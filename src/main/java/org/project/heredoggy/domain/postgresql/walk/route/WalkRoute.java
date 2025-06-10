package org.project.heredoggy.domain.postgresql.walk.route;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class WalkRoute {
    // 기본 경로 컴포넌트
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_name")
    private String routeName;

    private String description;

    private Double totalDistance; // 총 거리
    private Integer expectedDuration; // 분 단위

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Builder.Default
    @OneToMany(mappedBy = "walkRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutePoint> points = new ArrayList<>();

}