package org.project.heredoggy.user.posts.noticePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberNoticePostService {
    private final NoticePostRepository noticePostRepository;
    private final PostImageRepository postImageRepository;

    public List<ShelterNoticePostResponseDTO> getAllNoticePost() {

        List<NoticePost> lists = noticePostRepository.findAllOrderByCreatedAtDesc();

        return lists.stream()
                .map(post -> convertToDTO(post, List.of()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ShelterNoticePostResponseDTO getDetailNoticePost(Long postId) {

        NoticePost post = noticePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        noticePostRepository.save(post);

        List<String> imageUrls = postImageRepository.findByNoticePost(post).stream()
                .map(PostImage::getImageUrl)
                .toList();

        return convertToDTO(post, imageUrls);
    }

    private ShelterNoticePostResponseDTO convertToDTO(NoticePost post, List<String> imageUrls) {
        return ShelterNoticePostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .email(post.getWriter().getEmail())
                .nickname(post.getWriter().getNickname())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .imageUrls(imageUrls)
                .build();
    }
}
