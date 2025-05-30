package org.project.heredoggy.domain.postgresql.walk.route;

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

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private WalkRoute walkRoute;
}
