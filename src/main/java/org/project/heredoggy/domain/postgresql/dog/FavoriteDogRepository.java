package org.project.heredoggy.domain.postgresql.dog;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteDogRepository extends JpaRepository<FavoriteDog, Long> {
    boolean existsByMemberAndDog(Member member, Dog dog);

    List<FavoriteDog> findByMember(Member member);
}
