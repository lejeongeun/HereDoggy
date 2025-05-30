package org.project.heredoggy.shelter.noticePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterNoticePostService {
    private final NoticePostRepository noticePostRepository;

    @Transactional
    public void createNoticePost(ShelterNoticePostRequestDTO request, CustomUserDetails userDetails) {
        Member shelterAdmin = AuthUtils.getValidMember(userDetails);

        NoticePost post = NoticePost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(shelterAdmin)
                .viewCount(0L)
                .build();

        noticePostRepository.save(post);
    }

    @Transactional
    public void editNoticePost(Long postId, ShelterNoticePostRequestDTO request, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        NoticePost post = noticePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(userDetails.getMember().getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        noticePostRepository.save(post);
    }


    @Transactional
    public void removeNoticePost(Long postId, CustomUserDetails userDetails) {
        String shelterEmail = AuthUtils.getValidMember(userDetails).getEmail();

        NoticePost post = noticePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(shelterEmail)) {
            throw new ConflictException("본인 글만 삭제할 수 있습니다.");
        }

        noticePostRepository.deleteById(postId);
    }


    public List<ShelterNoticePostResponseDTO> getAllNoticePost() {
        List<NoticePost> lists = noticePostRepository.findAllOrderByCreatedAtDesc();

        return convertToDTOList(lists);
    }


    public ShelterNoticePostResponseDTO getDetailNoticePost(Long postId) {

        NoticePost post = noticePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        noticePostRepository.save(post);

        return convertToDTO(post);
    }

    private List<ShelterNoticePostResponseDTO> convertToDTOList(List<NoticePost> lists) {
        return lists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private ShelterNoticePostResponseDTO convertToDTO(NoticePost post) {
        return ShelterNoticePostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .email(post.getWriter().getEmail())
                .nickname(post.getWriter().getNickname())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .build();
    }
}
