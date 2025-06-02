package org.project.heredoggy.user.like.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.like.Like;
import org.project.heredoggy.domain.postgresql.post.like.LikeRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.NotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FreePostRepository freePostRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final MissingPostRepository missingPostRepository;
    private final NoticePostRepository noticePostRepository;
    private final NotificationFactory notificationFactory;

    @Transactional
    public boolean toggleLike(PostType postType, Long postId, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        Object post = getPostByType(postType, postId);

        //자유게시판
        if(post instanceof FreePost freePost) {
            if (likeRepository.existsByMemberAndFreePost(member, freePost)) {
                    likeRepository.deleteByMemberAndFreePost(member, freePost);
                    return false;
                } else {
                    likeRepository.save(Like.builder().freePost(freePost).member(member).build());

                    Member postWriter = freePost.getWriter();
                    if (!member.equals(postWriter)) {
                        notificationFactory.notifyLike(postWriter, member, convertToReferenceType(postType), postId);
                    }

                    return true;
                }
            //리뷰게시판
        } else if (post instanceof ReviewPost reviewPost) {
            if (likeRepository.existsByMemberAndReviewPost(member, reviewPost)) {
                likeRepository.deleteByMemberAndReviewPost(member, reviewPost);
                return false;
            } else {
                likeRepository.save(Like.builder().reviewPost(reviewPost).member(member).build());

                Member postWriter = reviewPost.getWriter();
                if (!member.equals(postWriter)) {
                    notificationFactory.notifyLike(postWriter, member, convertToReferenceType(postType), postId);
                }

                return true;
            }
            //실종/발견 게시판
        } else if (post instanceof MissingPost missingPost) {
            if (likeRepository.existsByMemberAndMissingPost(member, missingPost)) {
                likeRepository.deleteByMemberAndMissingPost(member, missingPost);
                return false;
            } else {
                likeRepository.save(Like.builder().missingPost(missingPost).member(member).build());

                Member postWriter = missingPost.getWriter();
                if (!member.equals(postWriter)) {
                    notificationFactory.notifyLike(postWriter, member, convertToReferenceType(postType), postId);
                }

                return true;
            }
            //공지게시판
        } else if (post instanceof NoticePost noticePost) {
            if (likeRepository.existsByMemberAndNoticePost(member, noticePost)) {
                likeRepository.deleteByMemberAndNoticePost(member, noticePost);
                return false;
            } else {
                likeRepository.save(Like.builder().noticePost(noticePost).member(member).build());

                Member postWriter = noticePost.getWriter();
                if (!member.equals(postWriter)) {
                    notificationFactory.notifyLike(postWriter, member, convertToReferenceType(postType), postId);
                }

                return true;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 게시글 타입입니다.");
    }

    public Long getLikeCount(PostType postType, Long postId) {
        Object post = getPostByType(postType, postId);

        if(post instanceof FreePost freePost) {
            return likeRepository.countByFreePost(freePost);
        } else if (post instanceof ReviewPost reviewPost) {
            return likeRepository.countByReviewPost(reviewPost);
        } else if (post instanceof MissingPost missingPost) {
            return likeRepository.countByMissingPost(missingPost);
        } else if (post instanceof NoticePost noticePost) {
            return likeRepository.countByNoticePost(noticePost);
        }
        throw new IllegalArgumentException("지원하지 않는 게시글 타입입니다.");
    }

    private Object getPostByType(PostType type, Long postId) {
        return switch (type) {
            case FREE -> freePostRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("해당 게시물은 존재하지 않습니다."));
            case MISSING -> missingPostRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("해당 게시물은 존재하지 않습니다."));
            case REVIEW -> reviewPostRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("해당 게시물은 존재하지 않습니다."));
            case NOTICE -> noticePostRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("해당 게시물은 존재하지 않습니다."));
        };
    }

    private ReferenceType convertToReferenceType(PostType postType) {
        return switch (postType) {
            case FREE -> ReferenceType.FREE_POST;
            case REVIEW -> ReferenceType.REVIEW_POST;
            case MISSING -> ReferenceType.MISSING_POST;
            case NOTICE -> ReferenceType.SYSTEM_NOTICE;
        };
    }
}
