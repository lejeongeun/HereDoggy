package org.project.heredoggy.domain.postgresql.shelter.shelter;

import org.project.heredoggy.domain.postgresql.shelter.notice.Notice;
import org.project.heredoggy.domain.postgresql.shelter.shelterRequest.ShelterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
