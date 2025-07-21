package org.project.heredoggy.dog.like.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogLike;
import org.project.heredoggy.domain.postgresql.dog.DogLikeRepository;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogLikeService {
    private final MemberRepository memberRepository;
    private final DogRepository dogRepository;
    private final DogLikeRepository dogLikeRepository;

    public boolean toggleDogLike(Long dogId, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        boolean existingLike = dogLikeRepository.existsByMemberIdAndDogId(member.getId(), dogId);
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        DogLike dogLike = DogLike.builder()
                .dog(dog)
                .member(member)
                .build();
        dogLikeRepository.save(dogLike);

        return true;
    }


}
