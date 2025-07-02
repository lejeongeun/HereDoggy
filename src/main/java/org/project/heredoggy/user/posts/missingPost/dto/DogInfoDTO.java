package org.project.heredoggy.user.posts.missingPost.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogInfoDTO {
    private Long dogId;
    private String name;
    private String gender;
    private int age;
    private double weight;
    private boolean isNeutered;
    private String personality;
    private String foundLocation;
    private String imageUrl;
}