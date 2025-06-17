package org.project.heredoggy.user.walk.walkRecord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordEndRequestDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordEndStatisticDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordResponseDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordStartRequestDTO;
import org.project.heredoggy.user.walk.walkRecord.service.MemberWalkRecordService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walk-records")
public class MemberWalkRecordController {
    private final MemberWalkRecordService walkRecordService;
    private final ObjectMapper objectMapper;

    @PostMapping("/start")
    public ResponseEntity<WalkRecordResponseDTO> startWalk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody WalkRecordStartRequestDTO startRequestDTO){
        WalkRecordResponseDTO recordRes = walkRecordService.startWalk(userDetails, startRequestDTO);
        return ResponseEntity.ok(recordRes);
    }

    @PostMapping(value = "/{walk_records_id}/end", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WalkRecordResponseDTO> endWalk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestPart("request") String requestJson,
                                                         @RequestPart(value = "image", required = false) MultipartFile image,
                                                         @PathVariable("walk_records_id") Long walkRecordsId){
        WalkRecordEndRequestDTO endRequestDTO;
        try{
            endRequestDTO = objectMapper.readValue(requestJson, WalkRecordEndRequestDTO.class);
        }catch (JsonProcessingException e){
            throw new BadRequestException("Json 파싱 실패");
        }

        WalkRecordResponseDTO recordRes = walkRecordService.endWalk(userDetails, endRequestDTO, image, walkRecordsId);
        return ResponseEntity.ok(recordRes);
    }

    // 해당 산책 내역 통계
    @GetMapping("/{walk_records_id}/end/statistic")
    public ResponseEntity<WalkRecordEndStatisticDTO> getEndWalkStatistic(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable("walk_records_id") Long walkRecordsId) {
        WalkRecordEndStatisticDTO statistic = walkRecordService.getEndWalkStatistic(userDetails, walkRecordsId);
        return ResponseEntity.ok(statistic);
    }
}
