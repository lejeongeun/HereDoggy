package org.project.heredoggy.domain.postgresql.walk.reservation;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMember(Member member);

    List<Reservation> findByShelterId(Long sheltersId);

    boolean existsByMemberAndWalkOptionAndStatusIn(Member member, WalkOption walkOption, List<WalkReservationStatus> pending);

    List<Reservation> findByDogIdAndDateAndStatusIn(Long dogid, LocalDate date, List<WalkReservationStatus> statuses);
}
