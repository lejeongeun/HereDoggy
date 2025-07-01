package org.project.heredoggy.dog.favoriteDog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.dog.FavoriteDog;
import org.project.heredoggy.domain.postgresql.dog.FavoriteDogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteDogService {
    private final FavoriteDogRepository favoriteDogRepository;
    private final MemberRepository memberRepository;
    private final DogRepository dogRepository;

    @Transactional
    public void favoriteDog(Long memberId, Long dogId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new NotFoundException("사용자의 정보가 없습니다."));
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(()-> new NotFoundException("강아지 정보가 없습니다."));

        if (favoriteDogRepository.existsByMemberAndDog(member, dog)){
            throw new IllegalArgumentException("이미 관심 등록된 강아지입니다.");
        }
        FavoriteDog favoriteDog = FavoriteDog.builder()
                .member(member)
                .dog(dog)
                .build();

        favoriteDogRepository.save(favoriteDog);
    }
}
