package org.project.heredoggy.domain.postgresql.walk.reservation;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnavailableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dog dog;

}
