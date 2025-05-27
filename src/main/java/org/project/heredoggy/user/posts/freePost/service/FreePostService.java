package org.project.heredoggy.user.posts.freePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.freePost.dto.FreePostRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreePostService {
    private final FreePostRepository freePostRepository;

    @Transactional
    public void createFreePost(FreePostRequestDTO request, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        FreePost post = FreePost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(member)
                .viewCount(0L)
                .build();

        freePostRepository.save(post);
    }

    @Transactional
    public void editFreePost(Long postId, FreePostRequestDTO request, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        FreePost post = freePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(userDetails.getMember().getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        freePostRepository.save(post);
    }


    @Transactional
    public void removeFreePost(Long postId, CustomUserDetails userDetails) {
        String email = AuthUtils.getValidMember(userDetails).getEmail();

        FreePost post = freePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(email)) {
            throw new ConflictException("본인 글만 삭제할 수 있습니다.");
        }

        freePostRepository.deleteById(postId);
    }


    public List<FreePostResponseDTO> getAllFreePosts(CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        List<FreePost> lists = freePostRepository.findAllOrderByCreatedAtDesc();

        return convertToDTOList(lists);
    }


    public FreePostResponseDTO getDetailFreePosts(Long postId, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        FreePost post = freePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        freePostRepository.save(post);

        return convertToDTO(post);
    }

    private List<FreePostResponseDTO> convertToDTOList(List<FreePost> lists) {
        return lists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private FreePostResponseDTO convertToDTO(FreePost post) {
        return FreePostResponseDTO.builder()
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
