package com.aptner.v3.board.category.dto;

import lombok.Getter;
import lombok.Setter;

public class DeleteCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private long id;
    }
}
