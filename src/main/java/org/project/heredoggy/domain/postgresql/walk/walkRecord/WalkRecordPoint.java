package org.project.heredoggy.domain.postgresql.walk.walkRecord;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private LocalDateTime recordAt; // 좌표 기록 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_record_id")
    private WalkRecord walkRecord;
}
