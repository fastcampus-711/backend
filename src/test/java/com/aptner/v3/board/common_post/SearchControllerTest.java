package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.complain.ComplainService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import com.aptner.v3.board.free_post.FreePostService;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.board.market.MarketService;
import com.aptner.v3.board.market.dto.MarketDto;
import com.aptner.v3.board.notice_post.NoticePostService;
import com.aptner.v3.board.notice_post.dto.NoticePostDto;
import com.aptner.v3.board.qna.QnaService;
import com.aptner.v3.board.qna.dto.QnaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("다 못만들었음")
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticePostService noticePostService;

    @MockBean
    private FreePostService freePostService;

    @MockBean
    private MarketService marketService;

    @MockBean
    private QnaService qnaService;

    @MockBean
    private ComplainService complainService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithUserDetails(value = "user1")
    @Test
    public void testGetPostListByCategoryId() throws Exception {
        Pageable pageable = PageRequest.of(0, 4);

        when(noticePostService.getPostList(any(BoardGroup.class), any(), anyString(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new NoticePostDto())));
        when(freePostService.getPostList(any(BoardGroup.class), any(), anyString(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new FreePostDto())));
        when(marketService.getPostList(any(BoardGroup.class), any(), anyString(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new MarketDto())));
        when(qnaService.getPostList(any(BoardGroup.class), any(), anyString(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new QnaDto())));
        when(complainService.getPostList(any(BoardGroup.class), any(), anyString(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(new ComplainDto())));

        mockMvc.perform(get("/boards/search")
                        .param("keyword", "a")
                        .param("limit", "4")
                        .param("page", "1")
                        .param("sort", "RECENT"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.NOTICES.count").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.FREES.count").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.MARKETS.count").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.QNAS.count").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.COMPLAINT.count").value(1));
    }

    @WithUserDetails(value = "user1")
    @Test
    public void testNoticeService() {
        String keyword = "a";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<NoticePostDto> postList = noticePostService.getPostList(BoardGroup.NOTICES, null, keyword, null, null, pageable);

        assertTrue(postList.isEmpty());
    }

}