package com.aptner.v3.board.big_category.dto;

import lombok.Getter;
import lombok.Setter;

public class DeleteBigCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private long id;
    }
}
