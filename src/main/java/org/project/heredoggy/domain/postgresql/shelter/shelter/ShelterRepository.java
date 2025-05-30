package org.project.heredoggy.domain.postgresql.shelter.shelter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findByRegionContaining(String region);
}
