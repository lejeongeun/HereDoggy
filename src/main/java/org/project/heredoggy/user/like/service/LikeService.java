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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FreePostRepository freePostRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final MissingPostRepository missingPostRepository;
    private final NoticePostRepository noticePostRepository;
    private final NotificationFactory notificationFactory;
    private final RedisTemplate redisTemplate;

    @Transactional
    @CacheEvict(value = "likeCount", key = "#postId + '_' + #postType")
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

    @Cacheable(value = "likeCount", key = "#postId + '_' + #postType")
    public Long getLikeCount(PostType postType, Long postId) {
        System.out.println("🔥 캐시 미스! DB에서 직접 조회: " + postId + "_" + postType);
        return switch (postType) {
            case FREE -> likeRepository.countByFreePostId(postId);
            case REVIEW -> likeRepository.countByReviewPostId(postId);
            case MISSING -> likeRepository.countByMissingPostId(postId);
            case NOTICE -> likeRepository.countByNoticePostId(postId);
            default -> throw new IllegalArgumentException("지원하지 않는 게시글 타입입니다.");
        };
    }
    public Map<Long, Long> getLikeCountsBatch(PostType postType, List<Long> postIds) {
        Map<Long, Long> result = new HashMap<>();

        // 캐시 key 생성
        List<String> keys = postIds.stream()
                .map(id -> id + "_" + postType)
                .toList();

        // 캐시에서 일괄 조회
        List<Object> cached = redisTemplate.opsForValue().multiGet(keys.stream()
                .map(k -> "heredoggy::cache::likeCount::" + k)
                .toList());

        List<Long> missedIds = new ArrayList<>();

        for (int i = 0; i < postIds.size(); i++) {
            Object value = cached.get(i);
            Long postId = postIds.get(i);
            if (value != null) {
                result.put(postId, Long.valueOf(value.toString()));
            } else {
                missedIds.add(postId);
            }
        }

        // 캐시 미스 항목은 DB 조회 후 캐싱
        for (Long missedId : missedIds) {
            Long count = switch (postType) {
                case FREE -> likeRepository.countByFreePostId(missedId);
                case REVIEW -> likeRepository.countByReviewPostId(missedId);
                case MISSING -> likeRepository.countByMissingPostId(missedId);
                case NOTICE -> likeRepository.countByNoticePostId(missedId);
            };
            redisTemplate.opsForValue().set("heredoggy::cache::likeCount::" + missedId + "_" + postType, count);
            result.put(missedId, count);
        }

        return result;
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
