package com.aptner.v3.board.common_post;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.common_post.dto.CommonPostDto;

public interface DtoFactory<Q extends CommonPostDto.CommonPostRequest, T extends CommonPostDto> {
    T createDto(BoardGroup boardGroup, CustomUserDetails user, Q request);
}
