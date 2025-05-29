package org.project.heredoggy.user.comment.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.domain.postgresql.comment.Comment;
import org.project.heredoggy.domain.postgresql.comment.CommentRepository;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.user.comment.dto.CommentResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final FreePostRepository freePostRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final MissingPostRepository missingPostRepository;
    private final NoticePostRepository noticePostRepository;

    @Transactional
    public void createComment(PostType postType, Long postId, String content, Member writer) {
        validatePostExistence(postType, postId);
        Comment comment = Comment.builder()
                .postType(postType)
                .postId(postId)
                .content(content)
                .writer(writer)
                .build();

        commentRepository.save(comment);
    }
    public List<CommentResponseDTO> getComments(PostType postType, Long postId) {
        validatePostExistence(postType, postId);
        List<Comment> lists = commentRepository.findByPostTypeAndPostIdOrderByCreatedAt(postType, postId);

        return lists.stream()
                .map(list -> CommentResponseDTO.builder()
                        .id(list.getId())
                        .postType(list.getPostType())
                        .postId(list.getPostId())
                        .content(list.getContent())
                        .email(list.getWriter().getEmail())
                        .nickname(list.getWriter().getNickname())
                        .createdAt(String.valueOf(list.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void editComments(String content, Long commentId, Member writer) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글은 존재 하지 않습니다."));

        validatePostExistence(comment.getPostType(), comment.getPostId());

        if(!comment.getWriter().getEmail().equals(writer.getEmail())) {
            throw new ConflictException("수정할 권한이 없습니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComments(Long commentId, Member writer) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글은 존재 하지 않습니다."));

        validatePostExistence(comment.getPostType(), comment.getPostId());

        if(!comment.getWriter().getEmail().equals(writer.getEmail())) {
            throw new ConflictException("수정할 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    private void validatePostExistence(PostType postType, Long postId) {
        boolean exists = switch (postType) {
            case FREE -> freePostRepository.existsById(postId);
            case REVIEW -> reviewPostRepository.existsById(postId);
            case MISSING -> missingPostRepository.existsById(postId);
            case NOTICE -> noticePostRepository.existsById(postId);
        };

        if (!exists) {
            throw new NotFoundException("해당 게시글이 존재하지 않습니다.");
        }
    }
}

