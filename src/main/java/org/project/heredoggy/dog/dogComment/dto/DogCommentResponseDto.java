package org.project.heredoggy.dog.dogComment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.dog.DogComment;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogCommentResponseDto {
    private Long id;
    private String content;
    private String writeName;
    private LocalDateTime createdAt;
    public static DogCommentResponseDto fromEntity(DogComment comment){
        return DogCommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writeName(comment.getMember().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
