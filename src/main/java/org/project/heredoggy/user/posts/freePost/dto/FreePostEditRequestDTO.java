package org.project.heredoggy.user.posts.freePost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FreePostEditRequestDTO {
    private String title;
    private String content;
    private List<Long> deleteImageIds;
}
