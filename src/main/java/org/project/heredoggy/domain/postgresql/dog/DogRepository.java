package org.project.heredoggy.domain.postgresql.dog;

import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByShelterId(Long sheltersId);

    // Dog 엔티티와 연관된 DogImage 엔티티, Shleter 엔티티를 한 번의 쿼리로(fetch join 하여) 함께 로드 (N+1 쿼리 문제 방지)
    @Query("SELECT d FROM Dog d JOIN FETCH d.images i JOIN FETCH d.shelter s")
    List<Dog> findAllWithImages();
}
