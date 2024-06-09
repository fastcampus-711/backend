package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.qna.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class PostSpecification<E extends CommonPost> {

    public static <E extends CommonPost> Specification<E> hasBoardGroup(BoardGroup boardGroup) {
        return (root, query, criteriaBuilder) -> {
            if (boardGroup != null) {
                log.debug("specification - dtype : {}", boardGroup.getTable());
                return criteriaBuilder.equal(root.get("dtype"), boardGroup.getTable());
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId != null && categoryId > 0) {
                log.debug("specification - categoryId : {}", categoryId);
                return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null) {
                log.debug("specification - status : {}", status);
                return criteriaBuilder.equal(root.get("status"), status);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword != null && !keyword.isEmpty()) {
                log.debug("specification - title : {}", keyword.toLowerCase());
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> hasAuthor(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId != null) {
                log.debug("specification - authorId : {}", authorId);
                return criteriaBuilder.equal(root.get("member").get("id"), authorId);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static <E extends CommonPost> Specification<E> isDuty(Boolean isDuty) {
        return (root, query, criteriaBuilder) -> {
            if (isDuty != null) {
                log.debug("specification - isDuty : {}", isDuty);
                return criteriaBuilder.equal(root.get("isDuty"), isDuty);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
