package com.aptner.v3.menu.menu.dto;

import com.aptner.v3.menu.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MenuDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String code;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String code;
        private String name;
        private List<Category> categories;
    }

}
