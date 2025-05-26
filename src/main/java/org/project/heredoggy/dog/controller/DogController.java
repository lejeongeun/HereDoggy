package org.project.heredoggy.dog.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogEditRequestDTO;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.service.DogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs")
public class DogController {
    private final DogService dogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> create(
            @RequestPart("dog") @Valid DogRequestDTO request,
            @RequestPart("images") List<MultipartFile> images) throws IOException {
        dogService.create(request, images);
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

    @PutMapping(value = "/{dogs_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> edit (
            @PathVariable("dogs_id") Long id,
            @RequestPart("dog") DogEditRequestDTO request,
            @RequestPart(name = "newImages", required = false) List<MultipartFile> newImages) throws IOException{
        dogService.edit(id, request, newImages);
        return ResponseEntity.ok(Map.of("message", "강아지 정보가 수정되었습니다."));

    }
    @DeleteMapping("/{dogs_id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("dogs_id") Long id){
        dogService.delete(id);
        return ResponseEntity.ok(Map.of("message", "정보가 삭제 되었습니다."));
    }



}
