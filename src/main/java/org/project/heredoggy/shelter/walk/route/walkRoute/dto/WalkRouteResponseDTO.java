package org.project.heredoggy.shelter.walk.route.walkRoute.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkRouteResponseDTO {
    private Long id;
    private String routeName;
    private String description;
    private LocalDateTime createdAt;
    private String sheltersId;
}