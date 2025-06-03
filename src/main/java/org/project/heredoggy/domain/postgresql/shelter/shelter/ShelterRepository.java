package org.project.heredoggy.domain.postgresql.shelter.shelter;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findByRegionContaining(String region);
    Optional<Shelter> findByShelterAdmin(Member member);
//    @Query("SELECT s FROM Shelter s JOIN FETCH s.dogs WHERE s.id = :shelterId AND s.member.id = :memberId")
//    Optional<Shelter> findWithDogsByIdAndMemberId(@Param("shelterId") Long sheltersId,
//                                                  @Param("memberId") Long membersId);
}
