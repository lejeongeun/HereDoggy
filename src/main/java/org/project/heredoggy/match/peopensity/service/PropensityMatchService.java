package org.project.heredoggy.match.peopensity.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.match.propensity.DogBreed;
import org.project.heredoggy.domain.postgresql.match.propensity.DogBreedRepository;
import org.project.heredoggy.domain.postgresql.match.propensity.SurveyAnswer;
import org.project.heredoggy.domain.postgresql.match.propensity.SurveyAnswerRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.match.peopensity.dto.MatchResponseDTO;
import org.project.heredoggy.match.peopensity.dto.RecommendedBreedDTO;
import org.project.heredoggy.match.peopensity.dto.SurveyAnswerRequestDTO;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropensityMatchService {

    private final SurveyAnswerRepository surveyRepository;
    private final DogBreedRepository dogBreedRepository;

    public void submitSurvey(CustomUserDetails userDetails, SurveyAnswerRequestDTO requestDTO) {
        Member member = userDetails.getMember();

        if (surveyRepository.findByMember(member).isPresent()){
            throw new IllegalArgumentException("이미 설문을 완료하였습니다.");
        }
        SurveyAnswer answer = SurveyAnswer.builder()
                .member(member)
                .firstTimeOwner(requestDTO.isFirstTimeOwner())
                .allergyConcern(requestDTO.isAllergyConcern())
                .size(requestDTO.getSize())
                .hairLossSensitivity(requestDTO.getHairLossSensitivity())
                .exerciseTime(requestDTO.getExerciseTime())
                .kidsInHouse(requestDTO.isKidsInHouse())
                .aloneTime(requestDTO.getAloneTime())
                .homeType(requestDTO.getHomeType())
                .medicalBudget(requestDTO.getMedicalBudget())
                .groomingWillingness(requestDTO.getGroomingWillingness())
                .trainingEffort(requestDTO.getTrainingEffort())
                .activityLevel(requestDTO.getActivityLevel())
                .build();

        surveyRepository.save(answer);
    }

    public MatchResponseDTO recommendBreeds(CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        SurveyAnswer answer = surveyRepository.findByMember(member)
                .orElseThrow(()-> new IllegalArgumentException("설문을 먼저 작성해 주세요."));

        List<DogBreed> allBreed = dogBreedRepository.findAll();

        List<RecommendedBreedDTO> result = allBreed.stream()
                .map(dog -> calculateScoreAndReason(answer, dog))
                .sorted(Comparator.comparingInt(RecommendedBreedDTO::getScore).reversed())
                .limit(3) // 상위 3개 추천
                .collect(Collectors.toList());

        return MatchResponseDTO.builder()
                .results(result)
                .build();

    }

    private RecommendedBreedDTO calculateScoreAndReason(SurveyAnswer s, DogBreed d) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (s.isAllergyConcern() && d.isHypoallergenic()) {
            score += 15;
            reasons.add("저알레르기 품종이라 알레르기 걱정을 줄여줍니다.");
        }

        if (s.getSize() == d.getSize()) {
            score += 15;
            reasons.add("선호하는 크기의 강아지로 실내 생활에 적합합니다.");
        }

        if (s.getExerciseTime().name().equals(d.getExerciseNeed().name())) {
            score += 10;
            reasons.add("운동량이 맞아 산책 루틴과 잘 어울립니다.");
        }

        if (s.isKidsInHouse() == d.isKidFriendly()) {
            score += 10;
            reasons.add("아이들과 잘 지내는 품종이라 가족 생활에 적합합니다.");
        }

        if (s.getAloneTime().name().equals(d.getIndependence().name())) {
            score += 10;
            reasons.add("혼자 있는 시간도 잘 견디는 성향이라 걱정이 적습니다.");
        }

        if (s.getMedicalBudget().name().equals(d.getMedicalCost().name())) {
            score += 10;
            reasons.add("병원비 부담이 적은 견종이라 경제적입니다.");
        }

        if (s.isFirstTimeOwner() == d.isGoodForFirstTimeOwner()) {
            score += 10;
            reasons.add("초보자도 쉽게 키울 수 있는 품종입니다.");
        }

        if (s.getTrainingEffort().name().equals(d.getTrainability().name())) {
            score += 5;
            reasons.add("훈련이 쉬운 견종으로 훈련 스트레스가 적습니다.");
        }

        if (s.getActivityLevel().name().equals(d.getActivityDemand().name())) {
            score += 5;
            reasons.add("활동적인 보호자와 잘 어울리는 견종입니다.");
        }

        if (s.getGroomingWillingness().name().equals(d.getGroomingNeed().name())) {
            score += 5;
            reasons.add("털 손질 빈도가 보호자의 관리 가능 수준에 맞습니다.");
        }

        if (s.getHairLossSensitivity().name().equals(d.getHairLoss().name())) {
            score += 5;
            reasons.add("털 빠짐 정도가 민감도에 적합한 견종입니다.");
        }

        return RecommendedBreedDTO.builder()
                .breedId(d.getId())
                .breedName(d.getBreedName())
                .score(score)
                .imageUrl(d.getImageUrl())
                .reason(String.join(" ", reasons))
                .build();
    }
}
