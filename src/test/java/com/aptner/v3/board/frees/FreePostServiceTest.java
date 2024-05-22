package com.aptner.v3.board.frees;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.frees.dto.FreePostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - 자유 게시판")
@ExtendWith(MockitoExtension.class)
class FreePostServiceTest {

    @InjectMocks
    CommonPostService<FreePost, FreePostDto.FreeCommonRequest, FreePostDto.FreeCommonResponse> commonPostService;

    @Mock
    private CommonPostRepository commonPostRepository;

    @Test
    void 등록실행() {
        FreePostDto.FreeCommonRequest req = FreePostDto.FreeCommonRequest.builder()
                .title("자유게시판 제목")
                .content("자유게시판 내용")
                .categoryId(1L)
                .build();

        commonPostService.createPost(req);
    }

}