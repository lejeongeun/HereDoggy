package org.project.heredoggy.user.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.inquiry.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.ShelterSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.inquiry.dto.InquiryMyResponseDTO;
import org.project.heredoggy.user.inquiry.dto.InquiryRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ImageService imageService;
    private final InquiryImageRepository inquiryImageRepository;
    private final ShelterRepository shelterRepository;
    private final ShelterSseNotificationFactory shelterSseNotificationFactory;

    @Transactional
    public void registerInquires(CustomUserDetails userDetails, InquiryRequestDTO request, List<MultipartFile> images) {
        Member member = AuthUtils.getValidMember(userDetails);

        InquiryTarget target;
        Shelter shelter = null;

        if (request.getTargetId() == null) {
            target = InquiryTarget.SYSTEM;
        } else {
            target = InquiryTarget.SHELTER;
            shelter = findShelter(request.getTargetId());
        }

        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .target(target)
                .shelter(shelter)
                .build();
        inquiryRepository.saveAndFlush(inquiry);

        if(target == InquiryTarget.SHELTER && shelter != null) {
            Member shelterAdmin = shelter.getShelterAdmin();
            shelterSseNotificationFactory.notifyInquiryToShelter(
                    shelterAdmin,
                    member.getName(),
                    request.getTitle(),
                    inquiry.getId());
        }

        if(images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if(image.isEmpty()) continue;
                try {
                    String imageUrl = imageService.saveInquiryImage(image, inquiry.getId());
                    InquiryImage inquiryImage = InquiryImage.builder()
                            .imageUrl(imageUrl)
                            .inquiry(inquiry)
                            .build();
                    inquiryImageRepository.save(inquiryImage);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생을 하였습니다.", e);
                }
            }
        }
    }

    public List<InquiryMyResponseDTO> getAllMyInquires(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        List<Inquiry> lists = inquiryRepository.findByMemberAndIsDeletedFalseOrderByCreatedAtDesc(member);
        return lists.stream()
                .map(inquiry -> convertDTO(inquiry, List.of()))
                .collect(Collectors.toList());
    }

    public InquiryMyResponseDTO getMyInquires(CustomUserDetails userDetails, Long inquiryId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 문의입니다."));

        if(!inquiry.getMember().getId().equals(member.getId())) {
            throw new ConflictException("본인의 문의 글이 아닙니다.");
        }

        List<String> images = inquiry.getImages()
                .stream()
                .map(InquiryImage::getImageUrl)
                .toList();

        return convertDTO(inquiry, images);
    }


    private InquiryMyResponseDTO convertDTO(Inquiry inquiry, List<String> images) {
        return InquiryMyResponseDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .imageUrls(images)
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteInquires(CustomUserDetails userDetails, Long inquiryId) {
        Member member = AuthUtils.getValidMember(userDetails);
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 문의입니다."));

        if(!inquiry.getMember().getId().equals(member.getId())) {
            throw new ConflictException("본인의 문의만 삭제할 수 있습니다.");
        }

        inquiry.setIsDeleted(true);
    }
    private Shelter findShelter(Long shelterId) {
        return shelterRepository.findById(shelterId)
                .orElseThrow(() -> new NotFoundException("해당 보호소를 찾을 수 없습니다. ID: " + shelterId));
    }

}
