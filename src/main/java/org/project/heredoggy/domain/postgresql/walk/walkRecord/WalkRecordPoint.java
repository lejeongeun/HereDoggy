package org.project.heredoggy.domain.postgresql.walk.walkRecord;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class WalkRecordPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_record_id")
    private WalkRecord walkRecord;

    private Double latitude;
    private Double longitude;
    private LocalDateTime recordAt; // 좌표 기록 시간
    private Integer sequence;


}
