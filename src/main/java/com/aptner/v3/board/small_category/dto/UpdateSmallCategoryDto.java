package com.aptner.v3.board.small_category.dto;

import com.aptner.v3.board.SmallCategoryName;
import lombok.Getter;
import lombok.Setter;

public class UpdateSmallCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private long id;
        private SmallCategoryName smallCategoryName;
    }
}
