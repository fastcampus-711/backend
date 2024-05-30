package com.aptner.v3.board.common_post.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@ToString(callSuper = true)
@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    Page<?> posts;
    List<Integer> paginationBarNumber;
    public static SearchDto of(Page<?> posts, List<Integer> paginationBarNumbers) {
        return SearchDto.builder()
                .posts(posts)
                .paginationBarNumber(paginationBarNumbers)
                .build();
    }

    @ToString(callSuper = true)
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchResponse {
        Page<?> posts;
        List<Integer> paginationBarNumber;
        public static SearchResponse from(SearchDto dto) {

            return SearchResponse.builder()
                    .posts(dto.getPosts())
                    .paginationBarNumber(dto.getPaginationBarNumber())
                    .build();
        }
    }

}
