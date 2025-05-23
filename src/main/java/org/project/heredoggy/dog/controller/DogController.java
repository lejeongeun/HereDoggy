package org.project.heredoggy.dog.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
