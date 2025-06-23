package org.project.heredoggy.match.peopensity.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.match.peopensity.dto.SurveyAnswerRequestDTO;
import org.project.heredoggy.match.peopensity.service.SurveyAnswerService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/match/survey")
@RequiredArgsConstructor
public class SurveyAnswerController {
    private final SurveyAnswerService surveyService;

    @GetMapping
    public ResponseEntity<Map<String, String>> submitSurvey(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody SurveyAnswerRequestDTO requestDTO){
        surveyService.submitSurvey(userDetails, requestDTO);
        return ResponseEntity.ok(Map.of("message", "설문 작성을 완료하였습니다."));
    }


}
