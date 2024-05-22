package com.aptner.v3.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategorySearchRequest(
        @NotBlank
        Long menuId
) {

}
