package org.project.heredoggy.domain.postgresql.shelter.shelter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterImageRepository extends JpaRepository<ShelterImage, Long> {
    List<ShelterImage> findAllByShelterId(Long shelterId);
}
