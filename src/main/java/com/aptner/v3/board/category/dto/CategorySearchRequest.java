package com.aptner.v3.board.category.dto;

import com.aptner.v3.category.BoardGroup;
import jakarta.validation.constraints.NotNull;

public record CategorySearchRequest(
        @NotNull
        BoardGroup boardGroup
) {

}

