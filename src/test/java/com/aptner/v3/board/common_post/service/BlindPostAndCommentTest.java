package com.aptner.v3.board.common_post.service;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mockMvc.perform(
            get(prefix + "/boards")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0:1].visible").value(false));
    }
}