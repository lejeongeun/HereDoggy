package org.project.heredoggy.domain.postgresql.dog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogCommentRepository extends JpaRepository<DogComment, Long> {
    List<DogComment> findByDogIdOrderByCreatedAtAsc(Long dogId);
}
