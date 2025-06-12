package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordWithDogResponseDTO extends WalkRecordResponseDTO{
    private Long dogId;
    private String dogName;
    private String shelterName;
}
