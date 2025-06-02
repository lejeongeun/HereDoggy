package org.project.heredoggy.user.posts.missingPost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostEditRequestDTO;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostRequestDTO;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissingPostService {
    private final MissingPostRepository missingPostRepository;
    private final PostImageRepository postImageRepository;
    private final ImageService imageService;

    @Transactional
    public void createMissingPost(MissingPostRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        if(images != null && images.size() > 5) {
            throw new BadRequestException("이미지는 최대 5장까지 업로드 가능합니다");
        }
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

        if(images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if(image.isEmpty()) continue;
                try {
                    String imageUrl = imageService.savePostImage(image, PostType.MISSING, post.getId());
                    PostImage postImage = PostImage.builder()
                            .imageUrl(imageUrl)
                            .missingPost(post)
                            .build();
                    postImageRepository.save(postImage);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생을 하였습니다.", e);
                }
            }
        }
    }

    @Transactional
    public void editMissingPost(Long postId, MissingPostEditRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
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

        if(request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
            for (Long imageId : request.getDeleteImageIds()) {
                PostImage image = postImageRepository.findById(imageId)
                        .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

                if(!image.getMissingPost().getId().equals(post.getId())) {
                    throw new IllegalArgumentException("해당 게시글의 이미지가 아닙니다");
                }
                imageService.deleteImage(image.getImageUrl());
                postImageRepository.delete(image);
            }
        }

        if(images != null && !images.isEmpty()) {
            long existingCount = postImageRepository.countByMissingPost(post);
            if(existingCount + images.size() > 5) {
                throw new BadRequestException("기존 이미지와 합쳐서 5장을 초과할 수 없습니다.");
            }
            for (MultipartFile image : images) {
                if(image.isEmpty()) continue;
                try {
                    String imageUrl = imageService.savePostImage(image, PostType.MISSING, post.getId());
                    PostImage postImage = PostImage.builder()
                            .imageUrl(imageUrl)
                            .missingPost(post)
                            .build();
                    postImageRepository.save(postImage);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생을 하였습니다.");
                }
            }
        }
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


    public List<MissingPostResponseDTO> getAllMissingPosts() {

        List<MissingPost> lists = missingPostRepository.findAllOrderByCreatedAtDesc();

        return lists.stream()
                .map(post -> convertToDTO(post, List.of()))
                .collect(Collectors.toList());
    }


    @Transactional
    public MissingPostResponseDTO getDetailMissingPosts(Long postId) {

        MissingPost post = missingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        List<String> imageUrls = postImageRepository.findByMissingPost(post).stream()
                .map(PostImage::getImageUrl)
                .toList();

        post.setViewCount(post.getViewCount() + 1);
        missingPostRepository.save(post);


        return convertToDTO(post, imageUrls);
    }

    private MissingPostResponseDTO convertToDTO(MissingPost post, List<String> images) {
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
                .imageUrls(images)
                .build();
    }
}
