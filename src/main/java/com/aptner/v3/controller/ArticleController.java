package com.aptner.v3.controller;

import com.aptner.v3.article.Article;
import com.aptner.v3.article.ArticleComment;
import com.aptner.v3.article.ArticleCommentDTO;
import com.aptner.v3.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return ResponseEntity.status(201).body(createdArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        Article updatedArticle = articleService.updateArticle(id, articleDetails);
        if (updatedArticle != null) {
            return ResponseEntity.ok(updatedArticle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{articleId}/comments")
    public List<ArticleCommentDTO> getComments(@PathVariable Long articleId) {
        return articleService.getComments(articleId);
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<ArticleComment> createComment(@PathVariable Long articleId, @RequestBody ArticleCommentDTO commentDetails) {
        ArticleComment createdComment = articleService.createComment(articleId, commentDetails);
        if (createdComment != null) {
            return ResponseEntity.status(201).body(createdComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ArticleComment> updateComment(@PathVariable Long commentId, @RequestBody ArticleComment commentDetails) {
        ArticleComment updatedComment = articleService.updateComment(commentId, commentDetails);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        articleService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}