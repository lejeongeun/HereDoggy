package org.project.heredoggy.user.posts.missingPost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostRequestDTO;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissingPostService {
    private final MissingPostRepository missingPostRepository;

    @Transactional
    public void createMissingPost(MissingPostRequestDTO request, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        MissingPost post = MissingPost.builder()
                .writer(member)
                .type(request.getType())
                .title(request.getTitle())
                .gender(request.getGender())
                .age(request.getAge())
                .weight(request.getWeight())
                .furColor(request.getFurColor())
                .feature(request.getFeature())
                .missingDate(LocalDate.parse(request.getMissingDate()))
                .missingLocation(request.getMissingLocation())
                .description(request.getDescription())
                .isContactPublic(request.getIsContactPublic())
                .viewCount(0L)
                .build();

        missingPostRepository.save(post);
    }

    @Transactional
    public void editMissingPost(Long postId, MissingPostRequestDTO request, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        MissingPost post = missingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(userDetails.getMember().getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setGender(request.getGender());
        post.setAge(request.getAge());
        post.setWeight(request.getWeight());
        post.setFurColor(request.getFurColor());
        post.setFeature(request.getFeature());
        post.setMissingDate(LocalDate.parse(request.getMissingDate()));
        post.setDescription(request.getDescription());
        post.setIsContactPublic(request.getIsContactPublic());

        missingPostRepository.save(post);
    }


    @Transactional
    public void removeMissingPost(Long postId, CustomUserDetails userDetails) {
        String email = AuthUtils.getValidMember(userDetails).getEmail();

        MissingPost post = missingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(email)) {
            throw new ConflictException("본인 글만 삭제할 수 있습니다.");
        }

        missingPostRepository.deleteById(postId);
    }


    public List<MissingPostResponseDTO> getAllMissingPosts(CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        List<MissingPost> lists = missingPostRepository.findAllOrderByCreatedAtDesc();

        return convertToDTOList(lists);
    }


    public MissingPostResponseDTO getDetailMissingPosts(Long postId, CustomUserDetails userDetails) {
        AuthUtils.getValidMember(userDetails);

        MissingPost post = missingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        missingPostRepository.save(post);

        return convertToDTO(post);
    }

    private List<MissingPostResponseDTO> convertToDTOList(List<MissingPost> lists) {
        return lists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private MissingPostResponseDTO convertToDTO(MissingPost post) {
        return MissingPostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .type(post.getType())
                .gender(post.getGender())
                .age(post.getAge())
                .weight(post.getWeight())
                .furColor(post.getFurColor())
                .feature(post.getFeature())
                .missingDate(String.valueOf(post.getMissingDate()))
                .missingLocation(post.getMissingLocation())
                .description(post.getDescription())
                .isContactPublic(post.getIsContactPublic())
                .build();
    }
}
