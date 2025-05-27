package org.project.heredoggy.domain.postgresql.dog;

import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    List<Dog> findByShelterId(Long sheltersId);
}
