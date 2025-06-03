package org.project.heredoggy.user.adoption.service;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.adoption.*;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.dog.DogStatus;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.ShelterSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.adoption.dto.AdoptionSurveyRequestDTO;
import org.project.heredoggy.user.adoption.dto.MemberAdoptionRequestDTO;
import org.project.heredoggy.user.adoption.dto.MemberAdoptionResponseDTO;
import org.project.heredoggy.user.notification.service.NotificationSseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberAdoptionService {
    private final AdoptionRepository adoptionRepository;
    private final MemberRepository memberRepository;
    private final DogRepository dogRepository;
    private final ShelterSseNotificationFactory sseNotificationFactory;

    @Transactional
    public void requestAdoption(CustomUserDetails userDetails, Long dogsId, MemberAdoptionRequestDTO request) {
        Member member = AuthUtils.getValidMember(userDetails);

        if (!memberRepository.findById(member.getId()).isPresent()){
            throw new NotFoundException(ErrorMessages.MEMBER_NOT_FOUND);
        }
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        // 입양 상태 유효성 검증
        if (dog.getStatus() == DogStatus.ADOPTED){
            throw new InvalidRequestStateException(ErrorMessages.ADOPTION_DOG_INVALID);
        }
        boolean isAlreadyAdoptionRequest = adoptionRepository.existsByMemberAndDog(member, dog);

        if (isAlreadyAdoptionRequest){
            throw new InvalidRequestStateException(ErrorMessages.ALREADY_ADOPTION_REQUEST);
        }

        Adoption adoption = Adoption.builder()
                .dog(dog)
                .member(member)
                .shelter(dog.getShelter())
                .isMarried(request.isMarried())
                .visitDate(request.getVisitDate())
                .visitTime(request.getVisitTime())
                .status(AdoptionStatus.PENDING)
                .build();

        AdoptionSurveyRequestDTO surveyRequest = request.getSurvey();

        AdoptionSurvey adoptionSurvey = AdoptionSurvey.builder()
                .hasPetExp(surveyRequest.getHasPetExp())
                .petExpDetails(surveyRequest.getPetExpDetails())
                .hasPetNow(surveyRequest.getHasPetNow())
                .petNowDetail(surveyRequest.getPetNowDetail())
                .family(surveyRequest.getFamily())
                .familyAgreement(surveyRequest.getFamilyAgreement())
                .reason(surveyRequest.getReason())
                .sharePhoto(surveyRequest.getSharePhoto())
                .commitEndOfLife(surveyRequest.getCommitEndOfLife())
                .careAfterAdopt(surveyRequest.getCareAfterAdopt())
                .agreeNeutering(surveyRequest.getAgreeNeutering())
                .agreeFee(surveyRequest.getAgreeFee())
                .adoption(adoption)
                .build();

        adoption.setSurvey(adoptionSurvey);
        adoptionRepository.save(adoption);

        sseNotificationFactory.notifyAdoptionRequest(
                dog.getShelter().getShelterAdmin(),
                dog.getName(),
                member.getNickname(),
                adoption.getId()
        );
    }

    public List<MemberAdoptionResponseDTO> getAllMyAdoptions(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        return adoptionRepository.findAllByMember(member).stream()
                .map(this::toAdoptionsDto)
                .collect(Collectors.toList());
    }

    public MemberAdoptionResponseDTO getDetailsMyAdoptions(CustomUserDetails userDetails, Long adoptionsId) {
        Member member = AuthUtils.getValidMember(userDetails);
        Adoption adoption = adoptionRepository.findById(adoptionsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.ADOPTION_NOT_FOUND));
        if (!adoption.getMember().getId().equals(member.getId())){
            throw new ForbiddenException(ErrorMessages.ADOPTION_FORBIDDEN);
        }
        return toAdoptionsDto(adoption);
    }

    private MemberAdoptionResponseDTO toAdoptionsDto(Adoption adoption){
        return MemberAdoptionResponseDTO.builder()
                .adoptionId(adoption.getId())
                .dogId(adoption.getDog().getId())
                .dogName(adoption.getDog().getName())
                .shelterName(adoption.getShelter().getName())
                .shelterPhone(adoption.getShelter().getPhone())
                .visitDate(adoption.getVisitDate())
                .visitTime(adoption.getVisitTime())
                .createdAt(adoption.getCreatedAt())
                .status(adoption.getStatus())
                .build();
    }


}
