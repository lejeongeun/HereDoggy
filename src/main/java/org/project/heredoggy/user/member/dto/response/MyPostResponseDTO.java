package org.project.heredoggy.user.member.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostResponseDTO {
    private List<PostDTO> freePosts;
    private List<PostDTO> reviewPosts;
    private List<PostDTO> missingPosts;
}
