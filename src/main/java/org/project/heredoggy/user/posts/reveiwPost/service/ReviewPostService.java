package org.project.heredoggy.user.posts.reveiwPost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.reveiwPost.dto.ReviewPostRequestDTO;
import org.project.heredoggy.user.posts.reveiwPost.dto.ReviewPostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewPostService {
    private final ReviewPostRepository reviewPostRepository;

    @Transactional
    public void createReviewPost(ReviewPostRequestDTO request, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        ReviewPost post = ReviewPost.builder()
                .title(request.getTitle())
                .type(request.getType())
                .rank(request.getRank())
                .content(request.getContent())
                .writer(member)
                .viewCount(0L)
                .build();

        reviewPostRepository.save(post);
    }

    @Transactional
    public void editReviewPost(Long postId, ReviewPostRequestDTO request, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        ReviewPost post = reviewPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(userDetails.getMember().getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setType(request.getType());
        post.setRank(request.getRank());

        reviewPostRepository.save(post);
    }


    @Transactional
    public void removeReviewPost(Long postId, CustomUserDetails userDetails) {
        String email = AuthUtils.getValidMember(userDetails).getEmail();

        ReviewPost post = reviewPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(email)) {
            throw new ConflictException("본인 글만 삭제할 수 있습니다.");
        }

        reviewPostRepository.deleteById(postId);
    }


    public List<ReviewPostResponseDTO> getAllReviewPosts() {
        List<ReviewPost> lists = reviewPostRepository.findAllOrderByCreatedAtDesc();
        return convertToDTOList(lists);
    }


    public ReviewPostResponseDTO getDetailReviewPosts(Long postId) {
        ReviewPost post = reviewPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        reviewPostRepository.save(post);

        return convertToDTO(post);
    }

    private List<ReviewPostResponseDTO> convertToDTOList(List<ReviewPost> lists) {
        return lists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private ReviewPostResponseDTO convertToDTO(ReviewPost post) {
        return ReviewPostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .type(post.getType())
                .content(post.getContent())
                .rank(post.getRank())
                .viewCount(post.getViewCount())
                .email(post.getWriter().getEmail())
                .nickname(post.getWriter().getNickname())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .build();
    }
}
