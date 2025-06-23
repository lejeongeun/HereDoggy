package org.project.heredoggy.match.peopensity.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.match.propensity.SurveyAnswer;
import org.project.heredoggy.domain.postgresql.match.propensity.SurveyAnswerRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.match.peopensity.dto.SurveyAnswerRequestDTO;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyAnswerService {
    private final SurveyAnswerRepository surveyRepository;

    public void submitSurvey(CustomUserDetails userDetails, SurveyAnswerRequestDTO requestDTO) {
        Member member = userDetails.getMember();

        if (surveyRepository.findByMember(member).isPresent()){
            throw new IllegalArgumentException("이미 설문을 완료하였습니다.");
        }
        SurveyAnswer answer = SurveyAnswer.builder()
                .member(member)
                .popularity(requestDTO.isPopularity())
                .firstTimeOwner(requestDTO.isFirstTimeOwner())
                .noiseTolerance(requestDTO.isNoiseTolerance())
                .allergyConcern(requestDTO.isAllergyConcern())
                .size(requestDTO.getSize())
                .hairLossSensitivity(requestDTO.getHairLossSensitivity())
                .exerciseTime(requestDTO.getExerciseTime())
                .kidsInHouse(requestDTO.isKidsInHouse())
                .barkingTolerance(requestDTO.getBarkingTolerance())
                .aloneTime(requestDTO.getAloneTime())
                .homeType(requestDTO.getHomeType())
                .medicalBudget(requestDTO.getMedicalBudget())
                .groomingWillingness(requestDTO.getGroomingWillingness())
                .trainingEffort(requestDTO.getTrainingEffort())
                .activityLevel(requestDTO.getActivityLevel())
                .build();

        surveyRepository.save(answer);
    }
}
