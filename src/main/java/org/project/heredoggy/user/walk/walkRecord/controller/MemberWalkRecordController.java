package org.project.heredoggy.user.walk.walkRecord.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordEndRequestDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordResponseDTO;
import org.project.heredoggy.user.walk.walkRecord.dto.WalkRecordStartRequestDTO;
import org.project.heredoggy.user.walk.walkRecord.service.MemberWalkRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/walk-records")
public class MemberWalkRecordController {
    private final MemberWalkRecordService walkRecordService;

    @PostMapping("/start")
    public ResponseEntity<WalkRecordResponseDTO> startWalk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody WalkRecordStartRequestDTO startRequestDTO){
        WalkRecordResponseDTO recordRes = walkRecordService.startWalk(userDetails, startRequestDTO);
        return ResponseEntity.ok(recordRes);
    }
    @PostMapping("/{walk_records_id}/end")
    public ResponseEntity<WalkRecordResponseDTO> endWalk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody WalkRecordEndRequestDTO endRequestDTO,
                                                         @PathVariable("walk_records_id") Long walkRecordsId){
        WalkRecordResponseDTO recordRes = walkRecordService.endWalk(userDetails, endRequestDTO, walkRecordsId);
        return ResponseEntity.ok(recordRes);
    }


}
