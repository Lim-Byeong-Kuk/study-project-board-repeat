package com.example.boardrepeat.service;

import com.example.boardrepeat.domain.Article;
import com.example.boardrepeat.domain.ArticleComment;
import com.example.boardrepeat.dto.ArticleCommentDto;
import com.example.boardrepeat.repository.ArticleCommentRepository;
import com.example.boardrepeat.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleRepository articleRepository;

    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.articleId());
            articleCommentRepository.save(dto.toEntity(article));
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패, 댓글의 게시글을 찾을 수 없습니다. - dto : {}", dto);
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) {
                articleComment.setContent(dto.content());
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 수정 실패, 댓글의 게시글을 찾을 수 없습니다. - dto : {}", dto);
        }
    }

    public void deleteArticleComment(Long articleCommentId) {
        articleCommentRepository.deleteById(articleCommentId);
    }
}
