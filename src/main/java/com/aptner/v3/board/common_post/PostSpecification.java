package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.complain.Complain;
import com.aptner.v3.board.complain.ComplainStatus;
import com.aptner.v3.board.market.Market;
import com.aptner.v3.board.market.MarketStatus;
import com.aptner.v3.board.qna.Qna;
import com.aptner.v3.board.qna.QnaStatus;
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

    public static <E extends CommonPost> Specification<E> hasStatus(Status status, BoardGroup boardGroup) {
        if (status == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        switch (boardGroup.getTable()) {
            case "MarketPost":
                log.debug("specification - market status : {} {}", status, boardGroup.getTable());
                return (Specification<E>) marketHasStatus((MarketStatus) status);
            case "ComplaintPost":
                log.debug("specification - complaint status : {} {}", status, boardGroup.getTable());
                return (Specification<E>) complainHasStatus((ComplainStatus) status);
            case "QnaPost":
                log.debug("specification - qna status : {} {}", status, boardGroup.getTable());
                return (Specification<E>) qnaHasStatus((QnaStatus) status);
            default:
                log.debug("specification - etc");
                return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
    }

    private static Specification<Market> marketHasStatus(MarketStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.treat(root, Market.class).get("status"), status);
    }

    private static Specification<Complain> complainHasStatus(ComplainStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.treat(root, Complain.class).get("status"), status);
    }

    private static Specification<Qna> qnaHasStatus(QnaStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.treat(root, Qna.class).get("status"), status);
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
