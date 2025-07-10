package org.project.heredoggy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.comment.*;
import org.project.heredoggy.domain.postgresql.member.*;
import org.project.heredoggy.domain.postgresql.notice.*;
import org.project.heredoggy.domain.postgresql.post.PostImage;
import org.project.heredoggy.domain.postgresql.post.PostImageRepository;
import org.project.heredoggy.domain.postgresql.post.free.*;
import org.project.heredoggy.domain.postgresql.post.like.*;
import org.project.heredoggy.domain.postgresql.post.missing.*;
import org.project.heredoggy.domain.postgresql.post.review.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDataInitializer {

    private final MemberRepository memberRepository;

    private final FreePostRepository freePostRepository;
    private final MissingPostRepository missingPostRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final NoticePostRepository noticePostRepository;
    private final PostImageRepository postImageRepository;

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @PostConstruct
    @Transactional
    public void init() {
        if (freePostRepository.count() > 0) {
            log.info("📌 게시글 초기화 생략: 이미 FreePost 데이터 존재");
            return;
        }

        List<Member> userMembers = memberRepository.findAllByRole(RoleType.USER);
        if (userMembers.isEmpty()) throw new RuntimeException("❌ 일반 유저가 존재하지 않습니다.");

        Member shelterAdmin = memberRepository.findFirstByRole(RoleType.SHELTER_ADMIN)
                .orElseThrow(() -> new RuntimeException("❌ SHELTER_ADMIN 권한 유저가 존재하지 않습니다."));

        generateFreePosts(userMembers);
        generateMissingPosts(userMembers);
        generateReviewPosts(userMembers);
        generateNoticePosts(shelterAdmin);
    }

    private void generateFreePosts(List<Member> users) {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Member writer = users.get(i % users.size());

            FreePost post = freePostRepository.save(
                    FreePost.builder()
                            .title("자유게시글 제목 " + i)
                            .content("자유게시글 내용입니다.")
                            .viewCount(0L)
                            .writer(writer)
                            .build()
            );

            generateImagesFromApi(post, null, null, null);
            generateCommentsAndLikes(writer, PostType.FREE, post.getId(), post, null, null, null);
        });
    }

    private void generateMissingPosts(List<Member> users) {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Member writer = users.get(random.nextInt(users.size()));

            MissingPost post = missingPostRepository.save(
                    MissingPost.builder()
                            .title("실종/발견 제목 " + i)
                            .description("이 강아지는 " + (i % 2 == 0 ? "실종" : "목격") + "되었습니다.")
                            .type(i % 2 == 0 ? MissingPostType.MISSING : MissingPostType.FOUND)
                            .gender(i % 2 == 0 ? DogGender.MALE : DogGender.FEMALE)
                            .age(1 + (i % 10))
                            .weight(3.5 + i)
                            .furColor(i % 2 == 0 ? "갈색" : "흰색")
                            .feature("특징 " + i + ": 귀가 쫑긋해요.")
                            .missingDate(LocalDate.now().minusDays(i))
                            .missingLocation("서울시 " + (i % 3 == 0 ? "강남구" : i % 3 == 1 ? "서초구" : "성동구") + " " + i + "번지")
                            .isContactPublic(i % 2 == 0)
                            .viewCount(0L)
                            .writer(writer)
                            .build()
            );

            generateImagesFromApi(null, post, null, null);
            generateCommentsAndLikes(writer, PostType.MISSING, post.getId(), null, post, null, null);
        });
    }

    private void generateReviewPosts(List<Member> users) {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            Member writer = users.get(i % users.size());

            ReviewPost post = reviewPostRepository.save(
                    ReviewPost.builder()
                            .title("산책/입양 후기 제목 " + i)
                            .content("후기 내용입니다.")
                            .type(ReviewPostType.ADOPTION)
                            .rank(5)
                            .viewCount(0L)
                            .writer(writer)
                            .build()
            );

            generateImagesFromApi(null, null, post, null);
            generateCommentsAndLikes(writer, PostType.REVIEW, post.getId(), null, null, post, null);
        });
    }

    private void generateNoticePosts(Member shelterAdmin) {
        Shelter shelter = shelterAdmin.getShelter();
        if (shelter == null) {
            throw new RuntimeException("❌ shelterAdmin과 연결된 Shelter가 존재하지 않습니다.");
        }

        IntStream.rangeClosed(1, 20).forEach(i -> {
            NoticePost post = noticePostRepository.save(
                    NoticePost.builder()
                            .title("공지사항 제목 " + i)
                            .content("공지사항 내용입니다.")
                            .viewCount(0L)
                            .writer(shelterAdmin)
                            .shelter(shelter) // ✅ Shelter 연결 추가
                            .build()
            );
            generateImagesFromApi(null, null, null, post);
            generateCommentsAndLikes(shelterAdmin, PostType.NOTICE, post.getId(), null, null, null, post);
        });
    }

    private void generateCommentsAndLikes(Member writer, PostType postType, Long postId,
                                          FreePost freePost, MissingPost missingPost,
                                          ReviewPost reviewPost, NoticePost noticePost) {

        // 댓글 5개
        IntStream.rangeClosed(1, 5).forEach(i ->
                commentRepository.save(Comment.builder()
                        .writer(writer)
                        .postType(postType)
                        .postId(postId)
                        .content("댓글 내용 " + i)
                        .build())
        );

        // 좋아요 1~5개 랜덤 생성
        int likeCount = random.nextInt(5) + 1;
        for (int i = 0; i < likeCount; i++) {
            Like.LikeBuilder builder = Like.builder().member(writer);
            if (freePost != null) builder.freePost(freePost);
            if (missingPost != null) builder.missingPost(missingPost);
            if (reviewPost != null) builder.reviewPost(reviewPost);
            if (noticePost != null) builder.noticePost(noticePost);
            likeRepository.save(builder.build());
        }
    }

    // 이미지 저장 메서드 추가
    private void generateImagesFromApi(FreePost freePost, MissingPost missingPost, ReviewPost reviewPost, NoticePost noticePost) {
        for (int i = 0; i < 2; i++) {
            try {
                String apiUrl = "https://dog.ceo/api/breeds/image/random";
                ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
                String imageUrl = (String) response.getBody().get("message");

                PostImage.PostImageBuilder builder = PostImage.builder().imageUrl(imageUrl);
                if (freePost != null) builder.freePost(freePost);
                if (missingPost != null) builder.missingPost(missingPost);
                if (reviewPost != null) builder.reviewPost(reviewPost);
                if (noticePost != null) builder.noticePost(noticePost);
                postImageRepository.save(builder.build());

            } catch (Exception e) {
                log.warn("❌ dog.ceo 이미지 불러오기 실패: {}", e.getMessage());
            }
        }
    }
}
