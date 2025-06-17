package org.project.heredoggy.user.walk.walkRecord.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.reservation.ReservationRepository;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecord;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordPoint;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordRepository;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordStatus;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRoute;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRouteRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.walkRecord.dto.*;
import org.project.heredoggy.user.walk.walkRecord.mapper.WalkRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberWalkRecordService {
    private final WalkRecordRepository recordRepository;
    private final ReservationRepository reservationRepository;
    private final WalkRouteRepository walkRouteRepository;
    private final WalkRecordMapper recordMapper;
    private final ImageService imageService;


    @Transactional
    public WalkRecordResponseDTO startWalk(CustomUserDetails userDetails, WalkRecordStartRequestDTO startRequestDTO) {
        Member member = AuthUtils.getValidMember(userDetails);

        Reservation reservation = reservationRepository.findById(startRequestDTO.getReservationId())
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        // 사용자가 선택한 경로 유효성 검사
        WalkRoute walkRoute = walkRouteRepository.findById(startRequestDTO.getWalkRouteId())
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_NOT_FOUND));

        if (!member.getId().equals(reservation.getMember().getId())){
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        WalkRecord walkRecord = WalkRecord.builder()
                .reservation(reservation)
                .walkRoute(walkRoute)
                .startTime(LocalDateTime.now())
                .status(WalkRecordStatus.IN_PROGRESS)
                .build();

        recordRepository.save(walkRecord);
        return recordMapper.toWalkRecordDto(walkRecord);
    }

    @Transactional
    public WalkRecordResponseDTO endWalk(CustomUserDetails userDetails, WalkRecordEndRequestDTO endRequestDTO, MultipartFile image, Long walkRecordsId) {
        Member member = AuthUtils.getValidMember(userDetails);
        WalkRecord walkRecord = recordRepository.findById(walkRecordsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_NOT_FOUND));

        List<WalkRecordPoint> recordPoints = new ArrayList<>();

        for (WalkRecordPointDTO point : endRequestDTO.getActualPath()) {
            recordPoints.add(WalkRecordPoint.builder()
                            .latitude(point.getLatitude())
                            .longitude(point.getLongitude())
                            .recordAt(point.getRecordedAt())
                            .walkRecord(walkRecord)
                    .build());
        }

        walkRecord.setActualDistance(endRequestDTO.getActualDistance());
        walkRecord.setActualDuration(endRequestDTO.getActualDuration());
        walkRecord.setActualPath(recordPoints);
        walkRecord.setEndTime(LocalDateTime.now());
        walkRecord.setStatus(WalkRecordStatus.COMPLETED);

        if (image != null && !image.isEmpty()){
            try{
                String imageUrl = imageService.saveWalkRecordImage(image, walkRecord.getId());
                walkRecord.setThumbnailUrl(imageUrl);
            } catch (IOException e){
                throw new RuntimeException("WalkRecord 썸네일 저장 실패", e);
            }
        }

        return recordMapper.toWalkRecordDto(walkRecord);
    }

    @Transactional(readOnly = true)
    public WalkRecordEndStatisticDTO getEndWalkStatistic(CustomUserDetails userDetails, Long walkRecordsId) {
        Member member = AuthUtils.getValidMember(userDetails);
        WalkRecord walkRecord = recordRepository.findById(walkRecordsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_NOT_FOUND));

        if (!walkRecord.getReservation().getMember().getId().equals(member.getId())){
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        return WalkRecordEndStatisticDTO.builder()
                .actualDistance(walkRecord.getActualDistance())
                .actualDuration(walkRecord.getActualDuration())
                .thumbnailUrl(walkRecord.getThumbnailUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public List<WalkRecordResponseDTO> getAllWalkRecords(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        return recordRepository.findAll().stream()
                .map(recordMapper::toWalkRecordDto)
                .collect(Collectors.toList());
    }

    public WalkSimpleStatisticDTO getWalkStatistics(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        return recordRepository.getSimpleStatisticByMemberId(member.getId());
    }
}
