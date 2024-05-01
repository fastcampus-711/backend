package com.aptner.v3.board.small_category.dto;

import lombok.Getter;
import lombok.Setter;

public class DeleteSmallCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private long id;
    }
}
