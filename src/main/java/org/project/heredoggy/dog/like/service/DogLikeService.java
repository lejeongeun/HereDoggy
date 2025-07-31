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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DogLikeService {
    private final MemberRepository memberRepository;
    private final DogRepository dogRepository;
    private final DogLikeRepository dogLikeRepository;

    /**
     *
     * @param dogId
     * @param userDetails
     * @return ture : 좋아요 등록, false : 좋아요 취소
     */

    public boolean toggleDogLike(Long dogId, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        // 해당 멤버와 강아지에 대해 좋아요가 이미 존재하는지 확인
        Optional<DogLike> existingLike = dogLikeRepository.findByMemberIdAndDogId(member.getId(), dogId);

        // 이미 좋아요가 존재한다면 취소 등록
        if (existingLike.isPresent()){
            dogLikeRepository.delete(existingLike.get());
            return false;
        } else {
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
    // 해당 강아지의 좋아요 수 반환
    public long getLikeCount(Long dogId) {
     return dogLikeRepository.countByDogId(dogId);
    }
}
