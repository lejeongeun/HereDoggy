package org.project.heredoggy.domain.postgresql.inquiry;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByMemberAndIsDeletedFalseOrderByCreatedAtDesc(Member member);
    List<Inquiry> findByShelterOrderByCreatedAtDesc(Shelter shelter);
    List<Inquiry> findByTargetOrderByCreatedAtDesc(InquiryTarget target);


    Optional<Inquiry> findByShelterAndId(Shelter shelter, Long id);
    Optional<Inquiry> findByIdAndTarget(Long id, InquiryTarget target);
}
