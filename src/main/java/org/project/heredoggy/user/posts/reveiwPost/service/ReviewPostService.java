package org.project.heredoggy.user.posts.reveiwPost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.reservation.ReservationRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.reveiwPost.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewPostService {
    private final ReviewPostRepository reviewPostRepository;
    private final PostImageRepository postImageRepository;
    private final ReservationRepository reservationRepository;
    private final ImageService imageService;

    @Transactional
    public void createReviewPost(ReviewPostRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        if(images != null && images.size() > 5) {
            throw new BadRequestException("이미지는 최대 5장까지 업로드 가능합니다.");
        }
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

        saveImages(images, post);
    }

    @Transactional
    public void editReviewPost(Long postId, ReviewPostEditRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
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

        deleteImages(request.getDeleteImageIds(), post);
        saveImageWithLimitCheck(images, post);
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

    private void saveImages(List<MultipartFile> images, ReviewPost post) {
        if(images == null || images.isEmpty()) return;

        for (MultipartFile image : images) {
            if(image.isEmpty()) continue;
            try {
                String imageUrl = imageService.savePostImage(image, PostType.REVIEW, post.getId());
                PostImage postImage = PostImage.builder()
                        .imageUrl(imageUrl)
                        .reviewPost(post)
                        .build();
                postImageRepository.save(postImage);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }
    }

    private void saveImageWithLimitCheck(List<MultipartFile> images, ReviewPost post ){
        if(images == null || images.isEmpty()) return;

        long existingCount = postImageRepository.countByReviewPost(post);
        if(existingCount + images.size() > 5) {
            throw new BadRequestException("기존 이미지와 합쳐서 5장을 초과할 수 없습니다.");
        }
        saveImages(images, post);
    }

    private void deleteImages(List<Long> deleteImageIds, ReviewPost post) {
        if(deleteImageIds == null || deleteImageIds.isEmpty()) return;

        for (Long imageId : deleteImageIds) {
            PostImage image = postImageRepository.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

            if(!image.getReviewPost().getId().equals(post.getId())) {
                throw new IllegalArgumentException("해당 게시글의 이미지가 아닙니다.");
            }
            imageService.deleteImage(image.getImageUrl());
            postImageRepository.delete(image);
        }

    }

    @Transactional(readOnly = true)
    public List<ReviewPostResDTO> getAllReviewPosts() {
        return reviewPostRepository.findAllOptimized();
    }


    @Transactional
    public ReviewPostResponseDTO getDetailReviewPosts(Long postId) {
        ReviewPost post = reviewPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 게시물입니다."));

        post.setViewCount(post.getViewCount() + 1);
        reviewPostRepository.save(post);

        List<String> imageUrls = postImageRepository.findByReviewPost(post).stream()
                .map(PostImage::getImageUrl)
                .toList();

        return convertToDTO(post, imageUrls);
    }

    @Transactional(readOnly = true)
    public ReviewDogInfoDTO getDogInfoFromReservation(CustomUserDetails userDetails, Long reservationId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Reservation reservation = reservationRepository.findByIdAndMember(reservationId, member)
                .orElseThrow(() -> new NotFoundException("해당 산책 기록이 존재하지 않습니다."));

        Dog dog = reservation.getDog();
        String thumbnail = dog.getImages().isEmpty() ? null : dog.getImages().get(0).getImageUrl();

        return ReviewDogInfoDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .gender(dog.getGender().name())
                .age(dog.getAge())
                .shelterName(dog.getShelter().getName())
                .status(dog.getStatus().name())
                .imageUrl(thumbnail)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewDogInfoDTO> getMyCompletedDogs(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        List<Reservation> reservations = reservationRepository.findByMemberAndStatus(member, WalkReservationStatus.COMPLETED);

        return reservations.stream()
                .map(res -> {
                    Dog dog = res.getDog();
                    String thumbnail = dog.getImages().isEmpty() ? null : dog.getImages().get(0).getImageUrl();
                    return ReviewDogInfoDTO.builder()
                            .id(dog.getId())
                            .name(dog.getName())
                            .gender(dog.getGender().name())
                            .age(dog.getAge())
                            .shelterName(dog.getShelter().getName())
                            .status(dog.getStatus().name())
                            .imageUrl(thumbnail)
                            .build();
                }).distinct()
                .collect(Collectors.toList());
    }

    private ReviewPostResponseDTO convertToDTO(ReviewPost post, List<String> imageUrls) {
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
                .imageUrls(imageUrls)
                .build();
    }
}
