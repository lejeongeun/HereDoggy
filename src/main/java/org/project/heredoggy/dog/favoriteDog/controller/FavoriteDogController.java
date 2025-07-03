package org.project.heredoggy.dog.favoriteDog.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.favoriteDog.dto.DogFavoriteResponseDTO;
import org.project.heredoggy.dog.favoriteDog.service.FavoriteDogService;
import org.project.heredoggy.dog.mapper.DogResponseMapper;
import org.project.heredoggy.dog.service.DogImageSimilarityService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dogs/{dog_id}/favorites")
@RequiredArgsConstructor
public class FavoriteDogController {
    private final FavoriteDogService favoriteDogService;
    private final DogImageSimilarityService dogImageSimilarityService;
    private final DogResponseMapper dogResponseMapper;

    @PostMapping
    public CompletableFuture<ResponseEntity<DogFavoriteResponseDTO>> favoriteDog(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("dog_id") Long dogId) {
        return favoriteDogService.favoriteDog(userDetails.getMember().getId(), dogId)
                .thenCompose(dog -> dogImageSimilarityService.findTop3SimilarDogs(dog, favoriteDogService.getAllDogs())
                        .thenApply(similarDogs -> ResponseEntity.ok(
                                dogResponseMapper.toFavoriteResponse(dog, similarDogs, "관심 동물이 등록되었습니다.")
                        )));
    }
}