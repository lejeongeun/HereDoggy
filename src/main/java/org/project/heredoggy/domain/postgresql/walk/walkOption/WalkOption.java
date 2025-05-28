package org.project.heredoggy.domain.postgresql.walk.walkOption;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkOption {
    // 보호소에서 생성해서 제공하는 예약 옵션(예약 가능한 강아지와 시간대 제공)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    // 연결된 유기견
    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @OneToMany(mappedBy = "walkOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

}