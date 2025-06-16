package org.project.heredoggy.systemAdmin.admin.service;

import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AdminAuthUtils;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.RequestStatus;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.ShelterRequest;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.ShelterRequestRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.admin.dto.ShelterRequestResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminShelterRequestService {

    private final ShelterRequestRepository requestRepository;
    private final ShelterRepository shelterRepository;

    public List<ShelterRequestResponseDTO> getAllRequest(CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        List<ShelterRequest> requestlist = requestRepository.findAllByStatus(RequestStatus.PENDING);

        return entityToDto(requestlist);
    }

    public List<ShelterRequestResponseDTO> getAllRequestResponse(CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        List<RequestStatus> statuses = List.of(RequestStatus.APPROVED, RequestStatus.REJECTED);
        List<ShelterRequest> requestlist = requestRepository.findAllByStatusIn(statuses);

        return entityToDto(requestlist);
    }

    @Transactional
    public void approveRequest(Long requestId, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ShelterRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("해당 요청을 찾을 수 없습니다."));

        if(request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 요청입니다.");
        }

        Member requester = request.getRequester();
        requester.setRole(RoleType.SHELTER_ADMIN);

        Shelter shelter = Shelter.builder()
                .shelterAdmin(requester)
                .email(requester.getEmail())
                .name(request.getShelterName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .region(extractRegion(request.getAddress()))
                .description(request.getDescription())
                .shelterCode(UUID.randomUUID().toString())
                .build();

        shelterRepository.save(shelter);
        request.setStatus(RequestStatus.APPROVED);
    }

    @Transactional
    public void rejectRequest(Long requestId, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ShelterRequest request = requestRepository.findById(requestId)
                        .orElseThrow(() -> new NotFoundException("해당 요청 사항을 찾을 수 없습니다."));

        request.setStatus(RequestStatus.REJECTED);
    }

    private List<ShelterRequestResponseDTO> entityToDto(List<ShelterRequest> requestlist) {
        return requestlist.stream()
                .map(req -> ShelterRequestResponseDTO.builder()
                        .requestId(req.getId())
                        .shelterAdmin(req.getRequester().getName())
                        .shelterName(req.getShelterName())
                        .phone(req.getPhone())
                        .description(req.getDescription())
                        .address(req.getAddress())
                        .region(extractRegion(req.getAddress()))
                        .email(req.getRequester().getEmail())
                        .status(req.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    private String extractRegion(String address) {
        if (address == null || address.isBlank()) return null;

        String trimmed = address.replaceAll("\\(\\d{5}\\)\\s*", "");
        String[] parts = trimmed.split(" ");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return null;
    }
}
