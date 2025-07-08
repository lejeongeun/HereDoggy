//package org.project.heredoggy.config;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.project.heredoggy.domain.postgresql.comment.Comment;
//import org.project.heredoggy.domain.postgresql.comment.CommentRepository;
//import org.project.heredoggy.domain.postgresql.comment.PostType;
//import org.project.heredoggy.domain.postgresql.member.Member;
//import org.project.heredoggy.domain.postgresql.member.MemberRepository;
//import org.project.heredoggy.domain.postgresql.member.RoleType;
//import org.project.heredoggy.domain.postgresql.post.PostImage;
//import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
//import org.project.heredoggy.domain.postgresql.post.free.FreePost;
//import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
//import org.project.heredoggy.domain.postgresql.post.like.Like;
//import org.project.heredoggy.domain.postgresql.post.like.LikeRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Random;
//import java.util.stream.IntStream;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class FreePostDataInitializer {
//
//    private final MemberRepository memberRepository;
//    private final FreePostRepository freePostRepository;
//    private final PostImageRepository postImageRepository;
//    private final CommentRepository commentRepository;
//    private final LikeRepository likeRepository;
//
//    private final Random random = new Random();
//
//    @PostConstruct
//    @Transactional
//    public void init() {
//        if (freePostRepository.count() > 0) {
//            log.info("📌 FreePost 초기화 생략: 이미 데이터 존재");
//            return;
//        }
//
//        List<Member> userMembers = memberRepository.findAllByRole(RoleType.USER);
//        if (userMembers.isEmpty()) throw new RuntimeException("❌ USER 권한의 회원이 없습니다.");
//
//        log.info("📌 FreePost 초기화 시작");
//
//        IntStream.rangeClosed(1, 500).forEach(i -> {
//            Member writer = userMembers.get(i % userMembers.size());
//
//            FreePost post = freePostRepository.save(
//                    FreePost.builder()
//                            .title("자유게시글 제목 " + i)
//                            .content("이것은 자유게시글 테스트 내용입니다.")
//                            .viewCount(0L)
//                            .writer(writer)
//                            .build()
//            );
//
//            // 이미지 2개 생성
//            postImageRepository.save(new PostImage(post, "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg"));
//            postImageRepository.save(new PostImage(post, "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg"));
//
//            // 댓글 15개 생성
//            IntStream.rangeClosed(1, 15).forEach(k ->
//                    commentRepository.save(Comment.builder()
//                            .writer(writer)
//                            .postType(PostType.FREE)
//                            .postId(post.getId())
//                            .content("댓글 " + k + " - 자유게시글 " + i)
//                            .build())
//            );
//
//            // 좋아요 7개 생성 (같은 유저가 여러 번 누르는 건 실제로는 불가능하지만 성능 측정용이므로 허용)
//            IntStream.range(0, 7).forEach(l ->
//                    likeRepository.save(Like.builder()
//                            .member(writer)
//                            .freePost(post)
//                            .build())
//            );
//        });
//
//        log.info("✅ FreePost 데이터 생성 완료");
//    }
//}