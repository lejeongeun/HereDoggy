package org.project.heredoggy.domain.postgresql.walk.walkRecord;

import jakarta.persistence.*;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRoute;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class WalkRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation; // 산책

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_route_id")
    private WalkRoute walkRoute;

    private Double actualDistance; // 실제 이동 거리
    private Integer actualDuration; // 실제 소요 시간
    private LocalDateTime startTime; // 산책 시작 시간
    private LocalDateTime endTime; // 산책 종료 시간
    @OneToMany(mappedBy = "walkRecord", cascade = CascadeType.ALL)
    private List<WalkRecordPoint> actualPath;
    @Enumerated(EnumType.STRING)
    private WalkRecordStatus status;


}
