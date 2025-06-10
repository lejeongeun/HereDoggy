package org.project.heredoggy.systemAdmin.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.inquiry.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.NotificationFactory;
import org.project.heredoggy.global.util.AdminAuthUtils;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryAnswerDTO;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryDetailResponseDTO;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryImageRepository inquiryImageRepository;
    private final NotificationFactory notificationFactory;

    public List<AdminInquiryResponseDTO> getAdminInquiries(CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);

        return inquiryRepository.findByTargetOrderByCreatedAtDesc(InquiryTarget.SYSTEM).stream()
                .map(inquiry -> AdminInquiryResponseDTO.builder()
                        .id(inquiry.getId())
                        .title(inquiry.getTitle())
                        .content(inquiry.getContent())
                        .status(inquiry.getStatus())
                        .senderName(inquiry.getMember().getName())
                        .senderEmail(inquiry.getMember().getEmail())
                        .createdAt(inquiry.getCreatedAt())
                        .build())
                .toList();
    }

    public AdminInquiryDetailResponseDTO getAdminInquiryDetail(Long inquiryId, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        Inquiry inquiry = inquiryRepository.findByIdAndTarget(inquiryId, InquiryTarget.SYSTEM)
                .orElseThrow(() -> new NotFoundException("해당 문의를 찾을 수 없습니다."));

        List<String> images = inquiryImageRepository.findAllByInquiryId(inquiry.getId()).stream()
                .map(InquiryImage::getImageUrl)
                .collect(Collectors.toList());

        return AdminInquiryDetailResponseDTO.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .senderName(inquiry.getMember().getName())
                .senderEmail(inquiry.getMember().getEmail())
                .imageUrls(images)
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    @Transactional
    public void inquiryAdminAnswer(Long inquiryId, AdminInquiryAnswerDTO request, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        Inquiry inquiry = inquiryRepository.findByIdAndTarget(inquiryId, InquiryTarget.SYSTEM)
                .orElseThrow(() -> new NotFoundException("해당 문의를 찾을 수 없습니다."));

        inquiry.setStatus(InquiryStatus.ANSWERED);
        inquiry.setAnsweredAt(LocalDateTime.now());

        String title = "📩 [시스템]문의에 대한 답변이 도착했어요!";
        String content = "문의 제목: " + inquiry.getTitle() + "\n답변 내용: " + request.getContent();

        notificationFactory.notifyInquiry(
                inquiry.getMember(),
                inquiryId,
                title,
                content
        );
    }
}
