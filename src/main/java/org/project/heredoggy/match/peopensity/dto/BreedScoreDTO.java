package org.project.heredoggy.match.peopensity.dto;

import org.project.heredoggy.domain.postgresql.match.propensity.DogBreed;

public record BreedScoreDTO(
        DogBreed breed, int score
){}
