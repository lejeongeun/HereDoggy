package org.project.heredoggy.domain.postgresql.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogLikeRepository extends JpaRepository<DogLike, Long> {
    boolean existsByMemberIdAndDogId(Long memberId, Long dogId);
}
