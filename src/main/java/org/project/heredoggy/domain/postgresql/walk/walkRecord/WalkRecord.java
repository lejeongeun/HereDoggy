package org.project.heredoggy.domain.postgresql.walk.walkRecord;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRoute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double actualDistance; // 실제 이동 거리
    private Integer actualDuration; // 실제 소요 시간
    private LocalDateTime startTime; // 산책 시작 시간
    private LocalDateTime endTime; // 산책 종료 시간

    @Column(name = "thumbnail_url")
    private String thumbnailUrl; // 경로 스크린샷 썸네일 이미지

    @Enumerated(EnumType.STRING)
    private WalkRecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation; // 산책 예약

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_route_id")
    private WalkRoute walkRoute; // 어떤 기본 경로를 따라 걸었는지

    @Builder.Default
    @OneToMany(mappedBy = "walkRecord", cascade = CascadeType.ALL)
    private List<WalkRecordPoint> actualPath = new ArrayList<>(); // 실제 이동 경로 좌표들

}