package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class BlindPostAndCommentTest {
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
    void ADMIN_댓글이_LIST의_0번째_INDEX에_있는지_확인() throws Exception {
        //given
        mockMvc.perform(
                        get(prefix + "/boards/1")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.comments[0:1].admin").value(true));
    }

    @Test
    void visible이_false일_경우_게시글_blind처리() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get(prefix + "/boards")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Map<String, Object>> content = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.data.posts.content");

        for (Map<String, Object> post : content) {
            if ((Boolean) post.get("visible") == false) {
                assertThat((String) post.get("title")).isEqualTo("비밀 게시글입니다.");
                break;
            }
        }
    }
}