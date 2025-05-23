package org.project.heredoggy.domain.postgresql.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

}
