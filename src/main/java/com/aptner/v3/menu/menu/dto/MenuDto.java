package com.aptner.v3.menu.menu.dto;

import com.aptner.v3.menu.category.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MenuDto {

    @Getter
    @AllArgsConstructor
    public static class MenuRequest {
        @NotBlank
        private String code;
        @NotBlank
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuResponse {
        private Long id;
        private String code;
        private String name;
        private List<Category> categories;
    }

}
