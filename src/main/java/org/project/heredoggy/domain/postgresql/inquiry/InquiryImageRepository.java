package org.project.heredoggy.domain.postgresql.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {
    List<InquiryImage> findAllByInquiryId(Long inquiryId);
}
