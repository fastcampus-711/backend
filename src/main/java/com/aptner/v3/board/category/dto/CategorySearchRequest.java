package com.aptner.v3.board.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategorySearchRequest(
        @NotBlank
        Long menuId
) {

}
