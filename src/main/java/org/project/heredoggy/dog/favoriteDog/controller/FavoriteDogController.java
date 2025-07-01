package org.project.heredoggy.dog.favoriteDog.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.favoriteDog.service.FavoriteDogService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dogs/{dog_id}/favorites")
public class FavoriteDogController {

    private final FavoriteDogService favoriteDogService;
    @PostMapping
    public ResponseEntity<Map<String, String>> favoriteDog(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable("dog_id") Long dogId){

        favoriteDogService.favoriteDog(userDetails.getMember().getId(), dogId);
        return ResponseEntity.ok(Map.of("message", "관심 동물이 등록되었습니다."));
    }

}
