package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.qna.Status;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification<E extends CommonPost> {

    public static <E extends CommonPost> Specification<E> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId != null && categoryId > 0) {
                return criteriaBuilder.equal(root.get("categoryId"), categoryId);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword != null && !keyword.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get("status"), status);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasBoardGroup(BoardGroup boardGroup) {
        return (root, query, criteriaBuilder) -> {
            if (boardGroup != null) {
                return criteriaBuilder.equal(root.get("dtype"), boardGroup.getTable());
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
