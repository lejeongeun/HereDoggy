package org.project.heredoggy.shelter.service;

import global.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.RequestStatus;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.ShelterRequest;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.ShelterRequestRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.dto.ShelterCreateRequestDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShelterRequestService {
    private final ShelterRequestRepository shelterRequestRepository;
    public void createRequest(ShelterCreateRequestDTO request, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        if(member.getRole() != RoleType.USER) {
            throw new IllegalArgumentException("보호소를 소유하고 있지 않는 회원만 보호소 생성 요청이 가능합니다.");
        }

        String fullAddress = String.format("(%s) %s %s",
                request.getZipcode(),
                request.getAddress1(),
                request.getAddress2()
        );
        ShelterRequest req = ShelterRequest.builder()
                .shelterName(request.getShelterName())
                .description(request.getDescription())
                .phone(request.getPhone())
                .address(fullAddress)
                .status(RequestStatus.PENDING)
                .requester(member)
                .build();

        shelterRequestRepository.save(req);
    }
}
