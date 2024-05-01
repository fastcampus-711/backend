package com.aptner.v3.board.big_category.dto;

import lombok.Getter;
import lombok.Setter;

public class UpdateBigCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private long targetId;
        private String to;
    }
}
