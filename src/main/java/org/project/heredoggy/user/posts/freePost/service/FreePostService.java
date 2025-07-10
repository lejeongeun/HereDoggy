package org.project.heredoggy.user.posts.freePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.CommentRepository;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.like.LikeRepository;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.freePost.dto.FreePostEditRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreePostService {
    private final FreePostRepository freePostRepository;
    private final PostImageRepository postImageRepository;
    private final ImageService imageService;

    @Transactional
    public void createFreePost(FreePostRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        if(images != null && images.size() > 5) {
            throw new BadRequestException("이미지는 최대 5장까지 업로드 가능합니다.");
        }
        Member member = AuthUtils.getValidMember(userDetails);

        FreePost post = FreePost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(member)
                .viewCount(0L)
                .build();

        freePostRepository.save(post);

        if(images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if(image.isEmpty()) continue;;
                try {
                    String imageUrl = imageService.savePostImage(image, PostType.FREE, post.getId());
                    PostImage postImage = PostImage.builder()
                            .imageUrl(imageUrl)
                            .freePost(post)
                            .build();
                    postImageRepository.save(postImage);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생을 하였습니다.", e);
                }

            }
        }
    }

    @Transactional
    public void editFreePost(Long postId, FreePostEditRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        Member member = AuthUtils.getValidMember(userDetails);

        FreePost post = freePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(member.getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        freePostRepository.save(post);

        if(request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
            for (Long imageId : request.getDeleteImageIds()) {
                PostImage image = postImageRepository.findById(imageId)
                        .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

                if(!image.getFreePost().getId().equals(post.getId())) {
                    throw new IllegalArgumentException("해당 게시글의 이미지가 아닙니다.");
                }
                imageService.deleteImage(image.getImageUrl());
                postImageRepository.delete(image);
            }
        }

        if(images != null && !images.isEmpty()) {
            long existingCount = postImageRepository.countByFreePost(post);
            if(existingCount + images.size() > 5) {
                throw new BadRequestException("기존 이미지와 합쳐서 5장을 초과할 수 없습니다.");
            }

            for (MultipartFile image : images) {
                if(image.isEmpty()) continue;
                try {
                    String imageUrl = imageService.savePostImage(image, PostType.FREE, post.getId());
                    PostImage postImage = PostImage.builder()
                            .imageUrl(imageUrl)
                            .freePost(post)
                            .build();
                    postImageRepository.save(postImage);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
                }
            }
        }

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

    @Transactional(readOnly = true)
    public List<FreePostResDTO> getAllFreePosts() {
        return freePostRepository.findAllProjected();
    }


    @Transactional
    public FreePostResponseDTO getDetailFreePosts(Long postId) {
        FreePost post = freePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        List<String> imageUrls = postImageRepository.findByFreePost(post).stream()
                        .map(PostImage::getImageUrl)
                        .toList();


        post.setViewCount(post.getViewCount() + 1);
        freePostRepository.save(post);

        return convertToDTO(post, imageUrls);
    }

    private FreePostResponseDTO convertToDTO(FreePost post, List<String> imageUrl) {
        return FreePostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .email(post.getWriter().getEmail())
                .nickname(post.getWriter().getNickname())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .imagesUrls(imageUrl)
                .build();
    }
}
