package org.project.heredoggy.domain.postgresql.shelter.shelterRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterRequestRepository extends JpaRepository<ShelterRequest, Long> {
    List<ShelterRequest> findAllByStatus(RequestStatus status);

    List<ShelterRequest> findAllByStatusIn(List<RequestStatus> status);
}
