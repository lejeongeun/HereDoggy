package org.project.heredoggy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.dog.*;
import org.project.heredoggy.domain.postgresql.member.*;
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
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final ShelterRepository shelterRepository;
    private final DogRepository dogRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplateBuilder restTemplateBuilder;
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
        List<Member> members = IntStream.rangeClosed(1, 5)
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
                                    : LocalDate.of(2000, i, i))
                            .role(isShelterAdmin ? RoleType.SHELTER_ADMIN : RoleType.USER)
                            .build();
                })
                .map(memberRepository::save)
                .toList();

        // 2. 보호소 2개 생성 (SHELTER_ADMIN에 연결)
        List<Shelter> shelters = IntStream.rangeClosed(1, 2)
                .mapToObj(i -> Shelter.builder()
                        .email("shelter" + i + "@gmail.com")
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
    }

    private String fetchRandomDogImageUrl() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> response = restTemplate.getForObject("https://dog.ceo/api/breeds/image/random", Map.class);
        return response != null ? response.get("message") : null;
    }

}
