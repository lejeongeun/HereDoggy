package org.project.heredoggy.dog.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogEditRequestDTO;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.service.DogService;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/{shelters_id}/dogs")
public class DogController {
    private final DogService dogService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> create(@PathVariable("shelters_id") Long sheltersId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @RequestPart("dog") String dogJson,
                                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) throws IOException {
        // 역직렬화 json -> 객체로
        ObjectMapper objectMapper = new ObjectMapper();
        DogRequestDTO request = objectMapper.readValue(dogJson, DogRequestDTO.class);

        dogService.create(sheltersId, userDetails, request, images);
        return ResponseEntity.ok(Map.of("message", "강아지가 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<DogResponseDTO>> getDogsByShelter(@PathVariable("shelters_id") Long sheltersId,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        List<DogResponseDTO> dogList = dogService.getDogsByShelter(sheltersId);
        return ResponseEntity.ok(dogList);
    }

    @GetMapping("/{dogs_id}")
    public ResponseEntity<DogResponseDTO> getDetailsDog(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable("shelters_id") Long sheltersId,
                                                        @PathVariable("dogs_id") Long dogsId) {
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        DogResponseDTO dogDetails = dogService.getDetailsDog(dogsId);
        return ResponseEntity.ok(dogDetails);
    }

    @PutMapping(value = "/{dogs_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> edit (@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable("shelters_id") Long sheltersId,
                                                     @PathVariable("dogs_id") Long dogsId,
                                                     @RequestPart("dog") String dogJson,
                                                     @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
                                                     @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds) throws IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        DogEditRequestDTO request = objectMapper.readValue(dogJson, DogEditRequestDTO.class);

        dogService.edit(sheltersId, userDetails, dogsId, request, newImages, deleteImageIds);
        return ResponseEntity.ok(Map.of("message", "강아지 정보가 수정되었습니다."));

    }
    @DeleteMapping("/{dogs_id}")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable("shelters_id") Long sheltersId,
                                                      @PathVariable("dogs_id") Long dogs_id){
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        dogService.delete(sheltersId, userDetails, dogs_id);
        return ResponseEntity.ok(Map.of("message", "정보가 삭제 되었습니다."));
    }



}
