package org.project.heredoggy.shelter.noticePost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShelterNoticePostEditRequestDTO {
    private String title;
    private String content;
    private List<Long> deleteImageIds;
}
