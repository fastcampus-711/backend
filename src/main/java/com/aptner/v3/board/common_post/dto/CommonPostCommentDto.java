package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.CommentDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CommonPostCommentDto {

    public static Set<CommentDto.CommentResponse> organizeChildComments(Set<CommentDto> commentDto) {
        // commentDtos -> CommentResponse
        Map<Long, CommentDto.CommentResponse> map = commentDto.stream()
                .map(CommentDto::toResponseDto)
                .collect(Collectors.toMap(CommentDto.CommentResponse::getCommentId, Function.identity()));

        log.debug("parent map: {}", map);
        map.values().stream()
                .filter(CommentDto.CommentResponse::hasParentComment)
                .forEach(comment -> {
                    log.debug("parent comment: {}", comment.getParentCommentId());
                    CommentDto.CommentResponse parent = map.get(comment.getParentCommentId());
                    log.debug("parent comment: {}", comment.getParentCommentId());
                    parent.getChildComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(
                                Comparator
                                        .comparing(CommentDto.CommentResponse::isTop).reversed() // QNA 댓글 상단 고정
                                        .thenComparing(CommentDto.CommentResponse::getCreatedAt)
                                        .thenComparingLong(CommentDto.CommentResponse::getCommentId)
                        )
                ));
    }

    public static Object toList(List<Category> categories) {
        // CategoryDtoResponse 변환
        Map<Long, CategoryDto.CategoryResponse> map = categories.stream()
                .map(CategoryDto.CategoryResponse::from)
                .collect(Collectors.toMap(CategoryDto.CategoryResponse::getId, Function.identity()));

        // Grouping
        Map<Long, List<CategoryDto.CategoryResponse>> groupByMenuId = categories.stream()
                .collect(
                        Collectors.groupingBy(
                                Category::getBoardGroup,
                                Collectors.mapping(
                                        category -> map.get(category.getId()),
                                        Collectors.toList()
                                )
                        )
                );

        if (groupByMenuId.size() == 1) {
            return groupByMenuId.values().iterator().next(); // List<CategoryDtoResponse>
        }
        return groupByMenuId;                                // Map<Long, List<CategoryDtoResponse>>
    }
}
