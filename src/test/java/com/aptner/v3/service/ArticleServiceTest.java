package com.aptner.v3.service;


import com.aptner.v3.article.Article;
import com.aptner.v3.article.ArticleComment;
import com.aptner.v3.article.ArticleCommentDTO;
import com.aptner.v3.repository.ArticleCommentRepository;
import com.aptner.v3.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    public void testGetAllArticles() {
        Article article1 = new Article();
        article1.setId(1L);
        article1.setTitle("Article 1");

        Article article2 = new Article();
        article2.setId(2L);
        article2.setTitle("Article 2");

        when(articleRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        List<Article> articles = articleService.getAllArticles();

        assertThat(articles).hasSize(2);
        assertThat(articles.get(0).getTitle()).isEqualTo("Article 1");
        assertThat(articles.get(1).getTitle()).isEqualTo("Article 2");
    }

    @Test
    public void testGetArticleById() {
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Article 1");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.getArticleById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Article 1");
    }

    @Test
    public void testCreateArticle() {
        Article article = new Article();
        article.setTitle("New Article");

        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article createdArticle = articleService.createArticle(article);

        assertThat(createdArticle.getTitle()).isEqualTo("New Article");
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    public void testUpdateArticle() {
        Article existingArticle = new Article();
        existingArticle.setId(1L);
        existingArticle.setTitle("Old Title");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        Article updatedArticle = articleService.updateArticle(1L, existingArticle);
        updatedArticle.setTitle("New Title");

        assertThat(updatedArticle.getTitle()).isEqualTo("New Title");
        verify(articleRepository, times(1)).save(existingArticle);
    }

    @Test
    public void testGetComments() {
        ArticleComment comment1 = new ArticleComment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");

        ArticleComment comment2 = new ArticleComment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");

        when(articleCommentRepository.findByArticleId(1L)).thenReturn(Arrays.asList(comment1, comment2));

        List<ArticleCommentDTO> comments = articleService.getComments(1L);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getContent()).isEqualTo("Comment 1");
        assertThat(comments.get(1).getContent()).isEqualTo("Comment 2");
    }

    @Test
    public void testCreateComment() {
        Article article = new Article();
        article.setId(1L);

        ArticleComment comment = new ArticleComment();
        comment.setContent("New Comment");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(comment);

        ArticleComment createdComment = articleService.createComment(1L, new ArticleCommentDTO());

        assertThat(createdComment.getContent()).isEqualTo("New Comment");
        verify(articleCommentRepository, times(1)).save(any(ArticleComment.class));
    }

    @Test
    public void testUpdateComment() {
        ArticleComment existingComment = new ArticleComment();
        existingComment.setId(1L);
        existingComment.setContent("Old Comment");

        when(articleCommentRepository.findById(1L)).thenReturn(Optional.of(existingComment));

        existingComment.setContent("Updated Comment");
        when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(existingComment);

        ArticleComment updatedComment = articleService.updateComment(1L, existingComment);

        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
        verify(articleCommentRepository, times(1)).save(existingComment);
    }

    @Test
    public void testDeleteComment() {
        ArticleComment comment = new ArticleComment();
        comment.setId(1L);

        when(articleCommentRepository.findById(1L)).thenReturn(Optional.of(comment));

        articleService.deleteComment(1L);

        verify(articleCommentRepository, times(1)).delete(comment);
    }
}
