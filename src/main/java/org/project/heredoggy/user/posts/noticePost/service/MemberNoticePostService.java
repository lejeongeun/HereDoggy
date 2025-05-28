package org.project.heredoggy.user.posts.noticePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberNoticePostService {
    private final NoticePostRepository noticePostRepository;

    public List<ShelterNoticePostResponseDTO> getAllNoticePost(CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        List<NoticePost> lists = noticePostRepository.findAllOrderByCreatedAtDesc();

        return convertToDTOList(lists);
    }


    public ShelterNoticePostResponseDTO getDetailNoticePost(Long postId, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

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
