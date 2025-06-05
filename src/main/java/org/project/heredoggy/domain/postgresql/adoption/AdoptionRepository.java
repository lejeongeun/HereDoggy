package org.project.heredoggy.domain.postgresql.adoption;


import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    boolean existsByMemberAndDog(Member member, Dog dog);
    List<Adoption> findAllByMember(Member member);

    List<Adoption> findAllByDogIdIn(List<Long> dogsIds);

    @Query("SELECT a FROM Adoption a WHERE a.dog.shelter.id = :shelterId")
    List<Adoption> findAllByShelterId(@Param("shelterId") Long sheltersId);


}
