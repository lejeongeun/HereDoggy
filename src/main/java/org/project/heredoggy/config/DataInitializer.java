package org.project.heredoggy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.comment.Comment;
import org.project.heredoggy.domain.postgresql.comment.CommentRepository;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.dog.*;
import org.project.heredoggy.domain.postgresql.member.*;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.like.Like;
import org.project.heredoggy.domain.postgresql.post.like.LikeRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.*;
import org.project.heredoggy.image.ImageService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final ShelterRepository shelterRepository;
    private final DogRepository dogRepository;
    private final PasswordEncoder passwordEncoder;
    private final FreePostRepository freePostRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

    @PostConstruct
    @Transactional
    public void initSafe() {
        try {
            init();
        } catch (Exception e) {
            log.error("초기 데이터 삽입 중 예외 발생: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void init() {
        if (memberRepository.count() > 0) {
            log.info("초기화 생략: 이미 회원 데이터 존재");
            return;
        }

        log.info(" 초기 데이터 삽입 시작");

        // 0. 관리자 계정
        Member admin = Member.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .name("관리자")
                .nickname("admin")
                .phone("010-0000-0000")
                .birth(LocalDate.of(2001, 1, 1))
                .address("(00000) 서울시 강남구 어드민로 1길")
                .role(RoleType.SYSTEM_ADMIN)
                .build();
        memberRepository.save(admin);

        // 1. 일반 회원 및 보호소 관리자 생성
        List<Member> members = IntStream.rangeClosed(1, 102)
                .mapToObj(i -> {
                    boolean isShelterAdmin = i <= 2;
                    return Member.builder()
                            .email("user" + i + "@gmail.com")
                            .password(passwordEncoder.encode("1234"))
                            .name(isShelterAdmin
                                    ? (i == 1 ? "강남보호소 관리자" : "서초보호소 관리자")
                                    : "테스터" + i)
                            .nickname(isShelterAdmin
                                    ? (i == 1 ? "gangnamAdmin" : "seochoAdmin")
                                    : "test" + i)
                            .phone("010-1234-000" + i)
                            .address("(12345) 서울시 중구 테스트로 " + i + "길")
                            .birth(isShelterAdmin
                                    ? (i == 1 ? LocalDate.of(1990, 1, 1) : LocalDate.of(1992, 5, 12))
                                    : LocalDate.of(2000, (i % 12) + 1, Math.min((i % 28) + 1, 28)))
                            .role(isShelterAdmin ? RoleType.SHELTER_ADMIN : RoleType.USER)
                            .build();
                })
                .map(memberRepository::save)
                .toList();

        // 2. 보호소 2개 생성 (SHELTER_ADMIN에 연결)
        List<Shelter> shelters = IntStream.rangeClosed(1, 2)
                .mapToObj(i -> Shelter.builder()
                        .name("보호소" + i)
                        .phone("02-0000-000" + i)
                        .address("서울시 강남구 보호소" + i + "번지")
                        .region("서울")
                        .description("테스트 보호소입니다.")
                        .shelterCode("SHELTER_CODE_" + i)
                        .shelterAdmin(members.get(i - 1))
                        .build())
                .map(shelterRepository::save)
                .toList();

        // 3. 강아지 5마리 생성
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Shelter shelter = shelters.get(i % shelters.size());

            Dog dog = Dog.builder()
                    .name("강아지" + i)
                    .age(1 + i)
                    .gender(i % 2 == 0 ? Gender.FEMALE : Gender.MALE)
                    .personality("밝고 활발해요.")
                    .weight(5.0 + i)
                    .isNeutered(i % 2 == 1)
                    .foundLocation("서울시 성동구")
                    .status(DogStatus.AVAILABLE)
                    .shelter(shelter)
                    .build();

            dog = dogRepository.save(dog); // ID 필요하므로 먼저 저장

            try {
                String imageUrl = fetchRandomDogImageUrl();
                String savedPath = imageService.saveDogImageFromUrl(imageUrl, shelter.getId(), dog.getId());

                DogImage dogImage = DogImage.builder()
                        .dog(dog)
                        .imageUrl(savedPath)
                        .build();

                dog.getImages().add(dogImage);
                dogRepository.save(dog); // 다시 저장 (연관관계)
            } catch (Exception e) {
                log.error("강아지 이미지 저장 실패", e);
            }
        });
        List<Member> normalUsers = members.stream()
                .filter(m -> m.getRole() == RoleType.USER)
                .toList();

        // 4. 자유게시글 + 댓글 + 좋아요 추가
        IntStream.rangeClosed(1, 500).forEach(i -> {
            Member writer = normalUsers.get(i % normalUsers.size());
            long randomViewCount = ThreadLocalRandom.current().nextInt(0, 1000);

            FreePost savedPost = freePostRepository.save(
                    FreePost.builder()
                            .title("테스트 제목 " + i)
                            .content("테스트 내용 " + i)
                            .writer(writer)
                            .viewCount(randomViewCount)
                            .build()
            );

            // 댓글 2개 추가
            IntStream.rangeClosed(1, 2).forEach(j -> {
                commentRepository.save(Comment.builder()
                        .writer(normalUsers.get((i + j) % normalUsers.size()))
                        .content("댓글 " + j + "번입니다.")
                        .postType(PostType.FREE)
                        .postId(savedPost.getId())
                        .build());
            });

            // 좋아요 3~10개 랜덤 추가
            int likeCount = ThreadLocalRandom.current().nextInt(3, 11);
            IntStream.rangeClosed(1, likeCount).forEach(k -> {
                likeRepository.save(Like.builder()
                        .member(normalUsers.get((i + k) % normalUsers.size()))
                        .freePost(savedPost)
                        .build());
            });
        });


    }

    private String fetchRandomDogImageUrl() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response = restTemplate.getForObject("https://dog.ceo/api/breeds/image/random", Map.class);
        return response != null ? response.get("message") : null;
    }

}
