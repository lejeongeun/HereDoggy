package org.project.heredoggy.domain.postgresql.shelter.shelterRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRequestRepository extends JpaRepository<ShelterRequest, Long> {
}
