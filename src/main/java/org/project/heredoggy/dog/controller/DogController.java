package org.project.heredoggy.dog.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
public class DogController {
    private final DogService dogService;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody DogRequestDTO request){
        dogService.create(request);
        return ResponseEntity.ok(Map.of("message", "강아지가 등록되었습니다."));
    }
    @GetMapping
    public ResponseEntity<List<DogResponseDTO>> getAllDog(){
        List<DogResponseDTO> dogList = dogService.getAllDog();
        return ResponseEntity.ok(dogList);
    }

    @GetMapping("/{dogs_id}")
    public ResponseEntity<DogResponseDTO> getDetailsDog(@PathVariable("dogs_id") Long id) {
        DogResponseDTO dogDetails = dogService.getDetailsDog(id);
        return ResponseEntity.ok(dogDetails);
    }

    @PutMapping("/{dogs_id}")
    public ResponseEntity<Map<String, String>> edit (@PathVariable("dogs_id") Long id,
                                                     @RequestBody DogRequestDTO request){
        dogService.edit(id, request);
        return ResponseEntity.ok(Map.of("message", "정보가 수정되었습니다."));

    }
    @DeleteMapping("/{dogs_id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("dogs_id") Long id){
        dogService.delete(id);
        return ResponseEntity.ok(Map.of("message", "정보가 삭제 되었습니다."));
    }



}
