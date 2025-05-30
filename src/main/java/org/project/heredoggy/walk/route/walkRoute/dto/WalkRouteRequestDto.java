package org.project.heredoggy.walk.route.walkRoute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkRouteRequestDto {
    @NotBlank(message = "코스명을 입력해 주세요")
    private String routeName;
    private String description;
}
