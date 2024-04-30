package com.aptner.v3.article.repository;

import com.aptner.v3.article.Article;
import com.aptner.v3.article.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

    // 게시글 ID에 따라 댓글 조회
    List<ArticleComment> findByArticleId(Long articleId);
}

