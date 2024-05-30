package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CommonPostServiceCountOfCommentsApplyTest {
    private MockMvc mockMvc;

    @LocalServerPort
    int port;

    String prefix;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CommonPostRepository commonPostRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();

        prefix = "http://localhost:" + port;
    }

    @Test
    void 게시물_단일_조회_시_댓글_수_반환_여부_확인() throws Exception {
        mockMvc.perform(
                        get(prefix + "/boards/1")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.count_of_comments").exists());
    }

    @Test
    void 댓글_추가_시_게시판의_countOfComments_변경_여부_확인() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "comment content");

        //when
        mockMvc.perform(
                post(prefix + "/boards/1/comments")
                        .content(jsonObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        CommonPostDto.CommonPostResponse commonPost =
                ((CommonPost) commonPostRepository.findById(1).orElseThrow())
                .toResponseDtoWithoutComments();
        long countOfComments = commonPost.getCountOfComments();

        mockMvc.perform(
                        get(prefix + "/boards/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count_of_comments").value(countOfComments));
    }

    @Test
    void 댓글_수정_시_게시판의_countOfComments_변경_여부_확인() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "updated content");

        //when
        mockMvc.perform(
                put(prefix + "/boards/1/comments/1")
                        .content(jsonObject.toJSONString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        CommonPostDto.CommonPostResponse commonPost =
                ((CommonPost) commonPostRepository.findById(1).orElseThrow())
                        .toResponseDtoWithoutComments();
        long countOfComments = commonPost.getCountOfComments();

        mockMvc.perform(
                get(prefix + "/boards/1")
        )
                .andExpect(jsonPath("$.data.count_of_comments").value(countOfComments));
    }

    @Test
    void 댓글_삭제_시_게시판의_countOfComments_변경_여부_확인() throws Exception {
        //given

        //when
        mockMvc.perform(
                delete(prefix + "/boards/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        CommonPostDto.CommonPostResponse commonPost =
                ((CommonPost) commonPostRepository.findById(1).orElseThrow())
                        .toResponseDtoWithoutComments();
        long countOfComments = commonPost.getCountOfComments();

        mockMvc.perform(
                        get(prefix + "/boards/1")
                )
                .andExpect(jsonPath("$.data.count_of_comments").value(countOfComments));
    }
}