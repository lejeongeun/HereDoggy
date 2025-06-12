package org.project.heredoggy.domain.postgresql.walk.walkRoute;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoutePoint {
    // WalkRoute 세부 경로
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;
    private Double lng;

    private int sequence;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_route_id", nullable = false)
    private WalkRoute walkRoute;
}
