package com.aptner.v3.board.category.dto;

import com.aptner.v3.board.category.CategoryName;
import lombok.Getter;
import lombok.Setter;

public class CreateCategoryDto {
    @Getter
    @Setter
    public static class Request {
        private CategoryName categoryName;
    }
}
