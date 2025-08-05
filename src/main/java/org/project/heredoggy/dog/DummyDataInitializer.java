package org.project.heredoggy.dog;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.dog.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements CommandLineRunner {
    private final DogRepository dogRepository;
    private final MemberRepository memberRepository;
    private final DogCommentRepository dogCommentRepository;
    private final DogLikeRepository dogLikeRepository;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Dog dog = dogRepository.findById(1L).orElseThrow();
        List<Member> members = memberRepository.findAll();

        // 댓글 1000건 세팅
        for (int i = 1; i <= 1000; i++) {
            Member randomMember = members.get(i % members.size());
            DogComment dogComment = DogComment.builder()
                    .dog(dog)
                    .member(randomMember)
                    .content(i + "번째 테스트 댓글")
                    .build();
            dogCommentRepository.save(dogComment);
        }
        // 좋아요 수 1000건 세팅
        for (int i = 1; i <= 1000; i++) {
            Member randomMember = members.get(i % members.size());

            DogLike dogLike = DogLike.builder()
                    .dog(dog)
                    .member(randomMember)
                    .build();

            dogLikeRepository.save(dogLike);
        }
        dog.setViewCount(dog.getViewCount() + 1000);
        dogRepository.save(dog);

        System.out.println("더미 데이터 생성 완료");

    }
}
