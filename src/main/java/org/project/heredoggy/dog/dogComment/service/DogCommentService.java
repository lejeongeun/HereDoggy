package org.project.heredoggy.dog.dogComment.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dogComment.dto.DogCommentRequestDto;
import org.project.heredoggy.dog.dogComment.dto.DogCommentResponseDto;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogComment;
import org.project.heredoggy.domain.postgresql.dog.DogCommentRepository;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogCommentService {
    private final DogCommentRepository commentRepository;
    private final DogRepository dogRepository;

    @Transactional
    public void createComment(Long dogId, Member member, DogCommentRequestDto requestDto) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        DogComment comment = DogComment.builder()
                .content(requestDto.getContent())
                .member(member)
                .dog(dog)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<DogCommentResponseDto> getAllComments(Long dogId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        return commentRepository.findByDogIdOrderByCreatedAtAsc(dog.getId()).stream()
                .map(DogCommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateComment(Long dogId, Member member, Long commentId, DogCommentRequestDto requestDto) {
        DogComment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("댓글이 존재하지 않습니다. 댓글을 작성하여 주세요"));

        if(!comment.getDog().getId().equals(dogId)){
            throw new ForbiddenException("해당 강아지의 댓글이 아닙니다.");
        }

        if (!comment.getMember().getId().equals(member.getId())){
            throw new ForbiddenException("다른 사용자의 댓글은 수정할 수 없습니다.");
        }

        comment.updateContent(requestDto.getContent());
    }
    @Transactional
    public void deleteComment(Long dogId, Member member, Long commentId) {
        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        DogComment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("댓글이 존재하지 않습니다."));

        if (!comment.getMember().getId().equals(member.getId())){
            throw new ForbiddenException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        if(!comment.getDog().getId().equals(dogId)){
            throw new ForbiddenException("해당 강아지의 댓글이 아닙니다.");
        }
        commentRepository.delete(comment);
    }

}
