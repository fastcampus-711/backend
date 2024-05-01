package com.aptner.v3.board.big_category.dto;

import com.aptner.v3.board.big_category.domain.BigCategory;
import lombok.Getter;
import lombok.Setter;

public class CreateBigCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private String name;

        public BigCategory toEntity() {
            return new BigCategory(name);
        }
    }
}
