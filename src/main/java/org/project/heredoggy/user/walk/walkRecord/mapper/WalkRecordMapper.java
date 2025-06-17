package org.project.heredoggy.user.walk.walkRecord.mapper;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecord;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordPoint;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordPointDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WalkRecordMapper {
    public WalkRecordResponseDTO toWalkRecordDto(WalkRecord walkRecord){
        return WalkRecordResponseDTO.builder()
                .id(walkRecord.getId())
                .reservationId(walkRecord.getReservation().getId())
                .walkRouteId(walkRecord.getWalkRoute().getId())
                .actualDistance(walkRecord.getActualDistance())
                .actualDuration(walkRecord.getActualDuration())
                .startTime(walkRecord.getStartTime())
                .endTime(walkRecord.getEndTime())
                .status(walkRecord.getStatus())
                .actualPath(walkRecord.getActualPath().stream()
                        .map(this::toWalkRecordPointDto)
                        .collect(Collectors.toList()))
                .thumbnailUrl(walkRecord.getThumbnailUrl())
                .build();
    }

    public WalkRecordPointDTO toWalkRecordPointDto(WalkRecordPoint recordPoint){
        return WalkRecordPointDTO.builder()
                .latitude(recordPoint.getLatitude())
                .longitude(recordPoint.getLongitude())
                .recordedAt(recordPoint.getRecordAt())
                .build();
    }
}
