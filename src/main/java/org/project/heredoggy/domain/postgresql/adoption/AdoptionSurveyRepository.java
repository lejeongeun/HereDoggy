package org.project.heredoggy.domain.postgresql.adoption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionSurveyRepository extends JpaRepository <AdoptionSurvey, Long>{
}
