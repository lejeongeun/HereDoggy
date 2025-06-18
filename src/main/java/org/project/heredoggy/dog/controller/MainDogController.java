package org.project.heredoggy.dog.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.dto.MainDogResponseDTO;
import org.project.heredoggy.dog.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}