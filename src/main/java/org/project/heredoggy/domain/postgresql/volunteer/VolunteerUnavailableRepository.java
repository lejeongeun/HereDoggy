package org.project.heredoggy.domain.postgresql.volunteer;

import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VolunteerUnavailableRepository extends JpaRepository<VolunteerUnavailableTime, Long> {
    List<VolunteerUnavailableTime> findByShelter(Shelter shelter);
    List<VolunteerUnavailableTime> findByShelterAndDate(Shelter shelter, LocalDate date);
    void deleteAllByShelter(Shelter shelter);
}
