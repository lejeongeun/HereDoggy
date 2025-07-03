package org.project.heredoggy.domain.postgresql.volunteer;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VolunteerReservationRepository extends JpaRepository<VolunteerReservation, Long> {
    List<VolunteerReservation> findByMember(Member member);

    boolean existsByShelterAndDateAndStartTimeAndEndTime(Shelter shelter, LocalDate date, LocalTime startTime, LocalTime endTime);

    List<VolunteerReservation> findByShelter(Shelter shelter);
}
