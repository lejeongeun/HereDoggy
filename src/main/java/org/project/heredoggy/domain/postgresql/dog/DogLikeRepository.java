package org.project.heredoggy.domain.postgresql.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DogLikeRepository extends JpaRepository<DogLike, Long> {
    Optional<DogLike> findByMemberIdAndDogId(Long memberId, Long dogId);
    long countByDogId(Long dogId);
}
