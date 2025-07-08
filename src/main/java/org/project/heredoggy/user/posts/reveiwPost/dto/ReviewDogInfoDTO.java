package org.project.heredoggy.user.posts.reveiwPost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDogInfoDTO {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String shelterName;
    private String status;
    private String imageUrl;
}
