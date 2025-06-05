package org.project.heredoggy.dog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogImageResponseDTO {
    private Long id;
    private String imageUrl;

}
