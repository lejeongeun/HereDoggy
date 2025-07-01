package org.project.heredoggy.match.peopensity.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.match.peopensity.dto.MatchResponseDTO;
import org.project.heredoggy.match.peopensity.dto.RecommendedBreedDTO;
import org.project.heredoggy.match.peopensity.dto.SurveyAnswerRequestDTO;
import org.project.heredoggy.match.peopensity.service.PropensityMatchService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class PropensityMatchController {
    private final PropensityMatchService surveyService;

    @PostMapping("/survey")
    public ResponseEntity<Map<String, String>> submitSurvey(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody SurveyAnswerRequestDTO requestDTO){
        surveyService.submitSurvey(userDetails, requestDTO);
        return ResponseEntity.ok(Map.of("message", "설문 작성을 완료하였습니다."));
    }
    @GetMapping("/recommend")
    public ResponseEntity<MatchResponseDTO> getRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails){
        MatchResponseDTO resultRecommend = surveyService.recommendBreeds(userDetails);
        return ResponseEntity.ok(resultRecommend);
    }

}
