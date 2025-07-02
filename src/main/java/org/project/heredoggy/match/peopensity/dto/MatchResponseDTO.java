package org.project.heredoggy.match.peopensity.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDTO {
    private List<RecommendedBreedDTO> results;
}
