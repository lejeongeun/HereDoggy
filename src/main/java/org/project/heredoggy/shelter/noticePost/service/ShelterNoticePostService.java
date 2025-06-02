package org.project.heredoggy.shelter.noticePost.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostEditRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterNoticePostService {
    private final NoticePostRepository noticePostRepository;
    private final PostImageRepository postImageRepository;
    private final ImageService imageService;

    @Transactional
    public void createNoticePost(ShelterNoticePostRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        if(images != null && images.size() > 5) {
            throw new BadRequestException("이미지는 최대 5장까지 업로드 가능합니다.");
        }
        Member shelterAdmin = AuthUtils.getValidMember(userDetails);

        NoticePost post = NoticePost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(shelterAdmin)
                .viewCount(0L)
                .build();

        noticePostRepository.save(post);

        saveImages(images, post);
    }

    @Transactional
    public void editNoticePost(Long postId, ShelterNoticePostEditRequestDTO request, CustomUserDetails userDetails, List<MultipartFile> images) {
        AuthUtils.getValidMember(userDetails);

        NoticePost post = noticePostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시물입니다."));

        if(!post.getWriter().getEmail().equals(userDetails.getMember().getEmail())) {
            throw new ConflictException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        noticePostRepository.save(post);
        deleteImages(request.getDeleteImageIds(), post);
        saveImageWithLimitCheck(images, post);
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

    private void saveImages(List<MultipartFile> images, NoticePost post) {
        if(images == null || images.isEmpty()) return;
        for (MultipartFile image : images) {
            try {
                String imageUrl = imageService.savePostImage(image, PostType.NOTICE, post.getId());
                PostImage postImage = PostImage.builder()
                        .imageUrl(imageUrl)
                        .noticePost(post)
                        .build();
                postImageRepository.save(postImage);
            } catch(IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생을 하였습니다.",e);
            }
        }
    }

    private void saveImageWithLimitCheck(List<MultipartFile> images, NoticePost post) {
        if(images == null || images.isEmpty()) return;

        long existingCount = postImageRepository.countByNoticePost(post);
        if(existingCount + images.size() > 5) {
            throw new BadRequestException("기존 이미지와 합쳐서 5장을 초과할 수 없습니다.");
        }
        saveImages(images, post);
    }
    private void deleteImages(List<Long> deleteImageIds, NoticePost post) {
        if(deleteImageIds == null || deleteImageIds.isEmpty()) return;

        for (Long imageId : deleteImageIds) {
            PostImage image = postImageRepository.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));

            if(!image.getNoticePost().getId().equals(post.getId())) {
                throw new IllegalArgumentException("해당 게시글의 이미지가 아닙니다.");
            }
            imageService.deleteImage(image.getImageUrl());
            postImageRepository.delete(image);
        }
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
