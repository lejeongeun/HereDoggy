package org.project.heredoggy.domain.postgresql.adoption;

import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    boolean existsByMemberAndDog(Member member, Dog dog);
    List<Adoption> findAllByMember(Member member);

}
