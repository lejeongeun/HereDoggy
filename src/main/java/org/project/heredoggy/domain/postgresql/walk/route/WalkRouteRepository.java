package org.project.heredoggy.domain.postgresql.walk.route;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WalkRouteRepository extends JpaRepository<WalkRoute, Long> {

    List<WalkRoute> findAllByShelterId(Long sheltersId);
    long countByShelterId(Long sheltersId);
}
