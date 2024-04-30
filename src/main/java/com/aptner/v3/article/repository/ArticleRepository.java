package com.aptner.v3.article.repository;

import com.aptner.v3.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
