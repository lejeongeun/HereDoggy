package org.project.heredoggy.domain.postgresql.walk.walkOption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkOptionRepository extends JpaRepository<WalkOption, Long> {
}
