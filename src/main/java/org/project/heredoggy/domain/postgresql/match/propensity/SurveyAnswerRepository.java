package org.project.heredoggy.domain.postgresql.match.propensity;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
    Optional<SurveyAnswer> findByMember(Member member);

}
