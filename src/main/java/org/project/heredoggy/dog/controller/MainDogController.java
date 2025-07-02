package org.project.heredoggy.dog.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.dto.MainDogResponseDTO;
import org.project.heredoggy.dog.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
public class MainDogController {
    private final DogService dogService;

    @GetMapping
    public ResponseEntity<List<MainDogResponseDTO>> getAllDogs(){
        List<MainDogResponseDTO> dogList = dogService.getAllDogs();
        return ResponseEntity.ok(dogList);
    }

    @GetMapping("/{dogs_id}")
    public ResponseEntity<MainDogResponseDTO> getDetailsDog(@PathVariable("dogs_id") Long dogsId){
        MainDogResponseDTO response = dogService.getDetailsDogMain(dogsId);
        return ResponseEntity.ok(response);
    }

    // 해당 보호소의 강아지 목록 조회 (사용자)
    @GetMapping("/shelters/{shelters_id}/another-shelters")
    public ResponseEntity<List<DogResponseDTO>> getAnotherShelterDogs(@PathVariable("shelters_id") Long sheltersId){
        List<DogResponseDTO> dogList = dogService.getAnotherShelterDogs(sheltersId);
        return ResponseEntity.ok(dogList);
    }
    /** // Javadoc 주석 시작
     * 사용자가 관심 등록한 강아지와 유사한 강아지들을 추천합니다.
     * 이 API는 특정 `FavoriteDog` 엔티티의 ID를 기준으로 유사한 강아지들을 찾아 반환합니다.
     * 예시 URL: GET /api/dogs/recommendations/similar-by-image?favoriteDogId=1&limit=3
     * @param favoriteDogId 사용자가 관심 등록한 강아지의 ID (FavoriteDog 엔티티의 ID)
     * @param limit 추천할 강아지의 최대 개수 (기본값: 3개로 설정되어 있습니다)
     * @return 유사 강아지 목록 (MainDogResponseDTO 형태로 반환)
     */
    @GetMapping("/recommendations/similar-by-image")
    public ResponseEntity<List<MainDogResponseDTO>> getSimilarDogsByImage(@RequestParam("favoriteDogId") Long favoriteDogId,
                                                                          @RequestParam(value = "limit", defaultValue = "3") int limit){
        List<MainDogResponseDTO> similarDogs = dogService.getTopSimilarDogs(favoriteDogId, limit);
        return ResponseEntity.ok(similarDogs);
    }

}